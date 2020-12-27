var crypto = require('crypto');
var aes_algorithm = "aes-128-ecb";

var encrypt = function (text, aes_secret) {
    var key = Buffer.from(aes_secret, 'base64');
    var cipher = crypto.createCipher(aes_algorithm, key);
    return Buffer.concat([cipher.update(text, 'utf8'), cipher.final()]).toString('base64');
};

var decrypt = function (text, aes_secret) {
    var key = Buffer.from(aes_secret, 'base64');
    var decipher = crypto.createDecipher(aes_algorithm, key);
    return Buffer.concat([decipher.update(text, 'base64'), decipher.final()]).toString('utf-8');
};

// var text = 'hello'
// var encrypted = encrypt(text, "ggininder") ;
// console.log("enc : ", encrypted);
// var decrypted = decrypt(encrypted, "ggininder");
// console.log("dec : ", decrypted);

module.exports = {
    encrypt : encrypt,
    decrypt : decrypt
}