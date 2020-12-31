const crypto = require('crypto');

function pem_to_der(pem) {
  return pem.split(' ').join('').split('-----BEGINRSAPUBLICKEY-----').join('').split('-----ENDRSAPUBLICKEY-----').join('').split('\n').join('')
}

function encrypt(toEncrypt, publicKey) {
  const buffer = Buffer.from(toEncrypt, 'utf-8');
  const encrypted = crypto.publicEncrypt(publicKey, buffer);
  return encrypted.toString('base64');
}

function decrypt(toDecrypt, privateKey, primaryPassword) {
  const buffer = Buffer.from(toDecrypt, 'base64');
  try {
    const decrypted = crypto.privateDecrypt(
      {
        key: privateKey.toString(),
        passphrase: primaryPassword,
      },
      buffer,
    );
    return decrypted.toString('utf8');
  } catch (e){
    console.log(e);
    return ""
  }
}

function decryptCallBack(toDecrypt, privateKey, primaryPassword, callBack) {
  const result = decrypt(toDecrypt, privateKey, primaryPassword);
  if(result == ""){
    throw "decrypt failed";
  } else {
    callBack(result);
  }
}

function generateKeys(primaryPassword) {
  const { privateKey, publicKey } = crypto.generateKeyPairSync('rsa', {
    modulusLength: 4096,
    publicKeyEncoding: {
      type: 'pkcs1',
      format: 'pem',
    },
    privateKeyEncoding: {
      type: 'pkcs8',
      format: 'pem',
      cipher: 'aes-256-cbc',
      passphrase: primaryPassword,
    },
  });

  return {
    PUBLIC_KEY : publicKey,
    PRIVATE_KEY : privateKey,
  };
}  

function generateKeysCallBack(primaryPassword, callBack) {
  const result =  generateKeys(primaryPassword);
  callBack(result);
}  

function sign(data, privateKey) {
  const dataBuf = Buffer.from(data);
  const signer = crypto.createSign('RSA-SHA512');
  signer.update(dataBuf);
  const signature = signer.sign(privateKey, 'hex');

  return signature;
}

function verify(data, publicKey, signature) {
  const dataBuf = Buffer.from(data);
  const verifier = crypto.createVerify('RSA-SHA512');
  verifier.update(dataBuf);
  const publicKeyBuf = Buffer.from(publicKey, 'utf-8');
  const signatureBuf = Buffer.from(signature, 'hex');
  const result       = verifier.verify(publicKeyBuf, signatureBuf);
  return result;
}

// generateKeysCallBack("password" , function(keyPair){
//   const enc = encrypt('hello', keyPair.PUBLIC_KEY);
//   console.log('enc', enc)
//   const dec = decrypt(enc, keyPair.PRIVATE_KEY, "password")
//   console.log('dec', dec)
// });

module.exports = {
  generateKeys : generateKeys,
  generateKeysCallBack : generateKeysCallBack,
  encrypt : encrypt,
  decrypt : decrypt,
  decryptCallBack : decryptCallBack,
  sign : sign, 
  verify : verify,
  pem_to_der : pem_to_der
}
