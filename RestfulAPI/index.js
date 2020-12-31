#!/usr/bin/env node
var crypto = require('crypto');
var uuid = require('uuid');
var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');
var rsa = require('./crypto/RSACoder');
var base64 = require('./crypto/Base64Coder');
const aes = require('./crypto/AESCoder');
var fs = require('fs');
// var session = require('express-session');
// const MongoStore = require('connect-mongo')(session);

var pool  = mysql.createPool({
    host: 'localhost',
    user: 'medical',
    password: 'ggininder',
    database: 'MedBigDataAI',
    // 無可用連線時是否等待pool連線釋放(預設為true)
    waitForConnections : true,
    // 連線池可建立的總連線數上限(預設最多為10個連線數)
    connectionLimit : 5
});

// PASSWORD UTIL
var geneRandomString =  function(length){
    return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex') /* convert to hexa format */
    .slice(0, length); /* return required number of charactors */
};

var sha512 = function(password, salt){
    var hash = crypto.createHmac('sha512', salt); // Use SHA512
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};

var checkUid = function (uid, callBack) {
    var res = null;
    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            return false;
        }
        else
        {
            con.query('SELECT * FROM user WHERE unique_id=?;', [uid], function (error, result, fields)  {
                if(error || !result || result.length!=1)
                    res = false;
                else 
                    res = true;
            })
        }
        con.release();
    });
    
    while (res == null);
    return res;
}

function saltHashPassword(userPassword){
    var salt = geneRandomString(16);
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}

function checkHashPassword(userPassword, salt)
{
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}



var app = express();
app.use(bodyParser.json()); // Accept JSON Params
app.use(bodyParser.urlencoded({extended: true})); // Accept URL Encoded params
//設置session相關設定
// app.use(session({
//     secret: 'recommand 128 bytes random string',
//     store:new MongoStore({url:'mongodb://localhost:27017/sessiondb'}),
//     resave: false,
//     saveUninitialized: true,
//     cookie: { maxAge: 600 * 1000 } //10分鐘到期
   
// }));

app.post('/register/', (req, res, next)=>{
    
    var post_data = req.body;

    var uid = uuid.v4();
    var plaint_password = post_data.password;
    var hash_data = saltHashPassword(plaint_password);
    var password = hash_data.passwordHash;
    var salt = hash_data.salt;

    var name = post_data.name;
    var email = post_data.email;
    
    pool.getConnection(function(error, con) {

        if (error) 
        {
            // 取得可用連線出錯
            res.json('[MySQL ERROR]' + error);  
        }
        else
        {
            con.query('SELECT * FROM user WHERE email=? OR name=?;', [email, name], function(err, result, fields){
                if(err){
                    res.json('[MySQL ERROR]' + err);
                }
                if(result && result.length)
                    res.json('User already exists!!!');
                else
                {
                    con.query('INSERT INTO MedBigDataAI.`user` (unique_id,name,email,encrypted_password,salt,created_at,updated_at) ' +
                        'VALUES (?,?,?,?,?,NOW(),NOW());', [uid, name, email, password, salt], function(err, result, fields){
                        if(err){
                            console.log('[MySQL ERROR]', err);
                            res.json('Register error: ', err);
                        }
                        res.json('Register successful');
                    });
                    
                }
            });
        }
        con.release();
    });
    
});

app.post('/login/', async function(req, res, next){

    var post_data = req.body;
    
    // Extract email and password from request
    var email = post_data.email;
    var name = post_data.email;
    var encryptedPassword = post_data.password;

    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            res.end(JSON.stringify("[MySQL error]" + error));
            return;
        }
        else
        {
            con.query('SELECT * FROM user WHERE name=? OR email=?', [name, email], function(error, result, fields){
                if(error){
                    res.end(JSON.stringify("[MySQL error]" + error));
                    return;
                }
        
                if(result && result.length==1)
                {
                    var private_key = fs.readFileSync(__dirname+'/keys/'+result[0].name+'.pem', {encoding:'utf-8'});
                    var salt = result[0].salt;
                    var encrypted_password = result[0].encrypted_password;
                    var uid = result[0].unique_id;
        
                    rsa.decryptCallBack(encryptedPassword, private_key, uid, function(decryptPassword){
                        // Hash password from Login request with salt in Database
                        var hashed_password = checkHashPassword(decryptPassword, salt).passwordHash;
        
                        if(encrypted_password == hashed_password)
                            res.end(JSON.stringify("Login successful")); // If password is true, return all info of user
                        else
                            res.end(JSON.stringify('Wrong password')); 
                    });
                }
                else
                {
                    res.json('User not exists!!!');
                }
            });
        }
        con.release();
    });
    
    
});

app.get('/getPatientList/', (req, res, next)=>{
    const query = req.query;
    const token = query.token;
    const user = query.user;
    
    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            res.end(JSON.stringify([]));
        }
        else
        {
            // 成功取得可用連線
            // 使用取得的連線
            con.query('SELECT * FROM user WHERE name=? OR email=?;', [user, user], function (error, result, fields) {
                if(error){
                    console.log('[MySQL error]', error);
                    res.end(JSON.stringify([]));
                }
                if(result && result.length==1) {
                    var private_key = fs.readFileSync(__dirname+'/keys/'+result[0].name+'.pem', {encoding:'utf-8'});
                    const name = result[0].name;
                    const email = result[0].email;
                    const uid = result[0].unique_id;
                    const encrypted_password = result[0].encrypted_password;
                    const salt = result[0].salt;
                    rsa.decryptCallBack(token, private_key, uid, function(message){
                        message = message.split(':');
                        if(message.length != 3)
                            res.end(JSON.stringify([]));
                        else{
                            var msgName = message[0];
                            var msgPassword = message[1];
                            var aesKey = message[2];
                            // Hash password from Login request with salt in Database
                            var hashed_password = checkHashPassword(msgPassword, salt).passwordHash;
        
                            if(encrypted_password == hashed_password && (msgName == name || msgName == email)){
                                con.query('SELECT name FROM patient_info;', function(error, result, fields){
                                    if(error){
                                        console.log('[MySQL error]', error);
                                        res.end(JSON.stringify([]));
                                    }
                                    res.end(aes.encrypt(JSON.stringify(result), aesKey));
                                });
                            }
                            else
                                res.end(JSON.stringify("Username or Password error")); 
                        }
                    });
                } 
            });
        }
        con.release();
    });
    
});

app.get('/getChol/', (req, res, next)=>{
    const query = req.query;
    const token = query.token;
    const user = query.user;
    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            res.end(JSON.stringify('[MySQL error]' + error));
        }
        else
        {
            // 成功取得可用連線
            // 使用取得的連線
            con.query('SELECT * FROM user WHERE name=? OR email=?;', [user, user], function (error, result, fields) {
                if(error){
                    res.end(JSON.stringify('[MySQL error]' + error));
                }
                if(result && result.length==1) {
                    var private_key = fs.readFileSync(__dirname+'/keys/'+result[0].name+'.pem', {encoding:'utf-8'});
                    const name = result[0].name;
                    const email = result[0].email;
                    const uid = result[0].unique_id;
                    const encrypted_password = result[0].encrypted_password;
                    const salt = result[0].salt;
                    rsa.decryptCallBack(token, private_key, uid, function(message){
                        message = message.split(':');
                        if(message.length != 4)
                            res.end(JSON.stringify("[Token error]"));
                        else{
                            var msgName = message[0];
                            var msgPassword = message[1];
                            var id = parseInt(message[2]);
                            var aesKey = message[3];
                            // Hash password from Login request with salt in Database
                            var hashed_password = checkHashPassword(msgPassword, salt).passwordHash;
        
                            if(encrypted_password == hashed_password && (msgName == name || msgName == email)){
                                con.query('SELECT chol FROM heart WHERE id=?;', [id], function(error, result, fields){
                                    if(error){
                                        console.log('[MySQL error]', error);
                                    }
                                    res.end(aes.encrypt(JSON.stringify(result), aesKey));
                                });
                            }
                            else
                                res.end(JSON.stringify("Credential error")); 
                        }
                    });
                } 
            });
        }
        con.release();
    });
    
});

app.get('/getBloodPressure/', (req, res, next)=>{
    const query = req.query;
    const token = query.token;
    const user = query.user;
    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            res.end(JSON.stringify('[MySQL error]' + error));
        }
        else
        {
            // 成功取得可用連線
            // 使用取得的連線
            con.query('SELECT * FROM user WHERE name=? OR email=?;', [user, user], function (error, result, fields) {
                if(error){
                    res.end(JSON.stringify('[MySQL error]' + error));
                }
                if(result && result.length==1) {
                    var private_key = fs.readFileSync('./keys/'+result[0].name+'.pem', {encoding:'utf-8'});
                    const name = result[0].name;
                    const email = result[0].email;
                    const uid = result[0].unique_id;
                    const encrypted_password = result[0].encrypted_password;
                    const salt = result[0].salt;
                    rsa.decryptCallBack(token, private_key, uid, function(message){
                        message = message.split(':');
                        if(message.length != 4)
                            res.end(JSON.stringify("[Token error]"));
                        else{
                            var msgName = message[0];
                            var msgPassword = message[1];
                            var id = parseInt(message[2]);
                            var aesKey = message[3];
                            // Hash password from Login request with salt in Database
                            var hashed_password = checkHashPassword(msgPassword, salt).passwordHash;
        
                            if(encrypted_password == hashed_password && (msgName == name || msgName == email)){
                                con.query('SELECT trestbps FROM heart WHERE id=?;', [id], function(error, result, fields){
                                    if(error){
                                        console.log('[MySQL error]', error);
                                    }
                                    res.end(aes.encrypt(JSON.stringify(result), aesKey));
                                });
                            }
                            else
                                res.end(JSON.stringify("Credential error")); 
                        }
                    });
                } 
            });
        }
        con.release();
    });
});

app.get('/getThalach/', (req, res, next)=>{
    const query = req.query;
    const token = query.token;
    const user = query.user;
    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            res.end(JSON.stringify('[MySQL error]' + error));
        }
        else
        {
            // 成功取得可用連線
            // 使用取得的連線
            con.query('SELECT * FROM user WHERE name=? OR email=?;', [user, user], function (error, result, fields) {
                if(error){
                    res.end(JSON.stringify('[MySQL error]' + error));
                }
                if(result && result.length==1) {
                    var private_key = fs.readFileSync('./keys/'+result[0].name+'.pem', {encoding:'utf-8'});
                    const name = result[0].name;
                    const email = result[0].email;
                    const uid = result[0].unique_id;
                    const encrypted_password = result[0].encrypted_password;
                    const salt = result[0].salt;
                    rsa.decryptCallBack(token, private_key, uid, function(message){
                        message = message.split(':');
                        if(message.length != 4)
                            res.end(JSON.stringify("[Token error]"));
                        else{
                            var msgName = message[0];
                            var msgPassword = message[1];
                            var id = parseInt(message[2]);
                            var aesKey = message[3];
                            // Hash password from Login request with salt in Database
                            var hashed_password = checkHashPassword(msgPassword, salt).passwordHash;
        
                            if(encrypted_password == hashed_password && (msgName == name || msgName == email)){
                                con.query('SELECT thalach FROM heart WHERE id=?;', [id], function(error, result, fields){
                                    if(error){
                                        console.log('[MySQL error]', error);
                                    }
                                    res.end(aes.encrypt(JSON.stringify(result), aesKey));
                                });
                            }
                            else
                                res.end(JSON.stringify("Credential error")); 
                        }
                    });
                } 
            });
        }
        con.release();
    });
});

app.get('/getPublicKey/', (req, res, next)=>{
    var name = req.query.name;
    const priPassword = uuid.v4(); 
    pool.getConnection(function(err, con) {

        if (err) 
        {
            // 取得可用連線出錯
            res.end(JSON.stringify("[MySQL] error: " + error));
        }
        else
        {
            rsa.generateKeysCallBack(priPassword, function(keyPair) {
                con.query('SELECT name FROM user WHERE name=? OR email=?', [name, name], function(error, results, fields){
                    if(error) {
                        res.end(JSON.stringify("[MySQL] error: " + error));
                    }
            
                    if(results && results.length == 1) {
                        name = results[0].name;
                        con.query('UPDATE user SET unique_id=?, updated_at=NOW()  WHERE name=? OR email=?;', [priPassword, name, name], function(error, results, fields){
                            if(error) {
                                res.end(JSON.stringify("[MySQL] error: "+ error.message));
                            } else {
                                fs.writeFile(__dirname+"/keys/"+name+".pem" , keyPair.PRIVATE_KEY, function(err){
                                    if(err)
                                        res.end(JSON.stringify("[WriteFile] error: " + err.message));
                                    res.end(rsa.pem_to_der(keyPair.PUBLIC_KEY)); 
                                });
                            }
                        });
                    } else {
                        res.end(JSON.stringify("getPublicKey error: " + "No such user"));
                    }
                });
            });
        }
        con.release();
    });
    
});

// Start Server
app.listen(3001, ()=>{
    console.log('MedApp Restful running on port 3001');
})
