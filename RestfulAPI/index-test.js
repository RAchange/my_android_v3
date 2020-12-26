var crypto = require('crypto');
var uuid = require('uuid');
var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');
var rsa = require('node-rsa');

// Connect to MySQL
var con = mysql.createConnection({
    host: 'localhost',
    user: 'medical',
    password: 'ggininder',
    database: 'MedBigDataAI'
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


app.post('/register/', (req, res, next)=>{
    
    var post_data = req.body;

    var uid = uuid.v4();
    var plaint_password = post_data.password;
    var hash_data = saltHashPassword(plaint_password);
    var password = hash_data.passwordHash;
    var salt = hash_data.salt;

    var name = post_data.name;
    var email = post_data.email;
    
    con.query('SELECT * FROM user WHERE email=? OR name=?;', [email, name], function(err, result, fields){
        if(err){
            console.log('[MySQL ERROR]', err);
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
});

app.post('/login/', (req, res, next)=>{
/*
    var post_data = req.body;
    
    // Extract email and password from request
    var email = post_data.email;
    var name = post_data.email;
    // var encrypted_password = base64.decodeHex(post_data.password);
    var keyPair;
    var decrypted_password;
    con.query('SELECT unique_id FROM user WHERE name=? OR email=?', [name, email], async function(error, result, fields){
        if(error){
            console.log('[MySQL ERROR]', error);
        }else{
            var primary_password = result[0].unique_id;
            keyPair = await rsa.makeKeyPair(primary_password);
            try {
                decrypted_password = await rsa.decrypt(encrypted_password, keyPair.privateKey, primary_password);    
            } catch (error) {
                res.end('Decrypt ERROR:' + error.message);
                return;
            }
            con.query('SELECT * FROM user where email=? OR name=?;', [email, name], function(error, result, fields){
                if(error){
                    console.log('[MySQL ERROR]', error);
                }
        
                if(result && result.length==1)
                {
                    var salt = result[0].salt;
                    var encoded_password = result[0].encrypted_password;
                    // Hash password from Login request with salt in Database
                    var hashed_password = checkHashPassword(decrypted_password.toString('utf-8'), salt).passwordHash;
                    if(encoded_password == hashed_password)
                        res.end(JSON.stringify('Login successful')); // If password is true, return all info of user
                    else
                        res.end(JSON.stringify('Wrong password'));
                }
                else
                {
                    res.json('User not exists!!!');
                }
            });
        }
    });
    */
   var post_data = req.body;
    
    // Extract email and password from request
    var user_password = post_data.password;
    var email = post_data.email;
    var name = post_data.email;

    con.query('SELECT * FROM user where email=? OR name=?;', [email, name], function(error, result, fields){
        if(error){
            console.log('[MySQL ERROR]', error);
        }

        if(result && result.length==1)
        {
            var salt = result[0].salt;
            var encrypted_password = result[0].encrypted_password;
            // Hash password from Login request with salt in Database
            var hashed_password = checkHashPassword(user_password, salt).passwordHash;
            if(encrypted_password == hashed_password)
                res.end(JSON.stringify("Login successful")); // If password is true, return all info of user
            else
                res.end(JSON.stringify('Wrong password'));
        }
        else
        {
            res.json('User not exists!!!');
        }
    });
});

app.get('/getPatientList/', (req, res, next)=>{
    con.query('SELECT name FROM patient_info;', function(error, result, fields){
        if(error){
            console.log('[MySQL ERROR]', error);
        }
        res.end(JSON.stringify(result));
    });
});

app.get('/getChol/', (req, res, next)=>{
    const id = req.query.id;
    con.query('SELECT chol FROM heart WHERE id=?;', [id], function(error, result, fields){
        if(error){
            console.log('[MySQL ERROR]', error);
        }
        res.end(JSON.stringify(result));
    });
});

app.get('/getBloodPressure/', (req, res, next)=>{
    const id = req.query.id;
    con.query('SELECT trestbps FROM heart WHERE id=?;', [id], function(error, result, fields){
        if(error){
            console.log('[MySQL ERROR]', error);
        }
        res.end(JSON.stringify(result));
    });
});

app.get('/getThalach/', (req, res, next)=>{
    const id = req.query.id;
    con.query('SELECT thalach FROM heart WHERE id=?;', [id], function(error, result, fields){
        if(error){
            console.log('[MySQL ERROR]', error);
        }
        res.end(JSON.stringify(result));
    });
});

app.get('/getPublicKey/', async (req, res, next)=>{
    const name = req.query.name;
    const priPassword = uuid.v4(); 
    const keyPair = await rsa.makeKeyPair(priPassword);

    con.query('UPDATE user SET unique_id=? WHERE name=? OR email=?;', [priPassword, name, name], function(error, results, fields){
        if(error) {
            res.json("/getPublicKey/", error.message);
        } else {
            res.end(base64.encode(keyPair.publicKey));
        }
    });
});

// Start Server
app.listen(3000, ()=>{
    console.log('MedApp Restful running on port 3000');
})
