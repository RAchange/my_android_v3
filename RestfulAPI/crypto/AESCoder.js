var crypto = require('crypto');

var encrypt = function (key, data) {
    var cipher = crypto.createCipher('aes-128-cbc', key);
    var crypted = cipher.update(data, 'utf8', 'binary');
    crypted += cipher.final('binary');
    crypted = new Buffer(crypted, 'binary').toString('base64');
    return crypted;
};

/**
 * 解密方法
 * @param key      解密的key
 * @param iv       向量
 * @param crypted  密文
 * @returns string
 */
var decrypt = function (key, crypted) {
    crypted = new Buffer(crypted, 'base64').toString('binary');
    var decipher = crypto.createDecipher('aes-128-cbc', key);
    var decoded = decipher.update(crypted, 'binary', 'utf8');
    decoded += decipher.final('utf8');
    return decoded;
};

// var key = '751f621ea5c8f930';
// console.log('加密的key:', key.toString('hex'));
// var iv = '2624750004598718';
// console.log('加密的iv:', iv);
// var data = "Hello, nodejs. 演示aes-128-cbc加密和解密";
// console.log("需要加密的資料:", data);
// var crypted = encrypt(key, iv, data);
// console.log("資料加密後:", crypted);
// var dec = decrypt(key, iv, crypted);
// console.log("資料解密後:", dec)

module.exports = {
    encrypt : encrypt,
    decrypt : decrypt
};