var encode = function(data){
    return Buffer.from(data).toString('base64');
}

var decode = function(data){
    return Buffer.from(data, 'base64').toString('utf-8');
}

var decodeHex = function (data) {
    return Buffer.from(data, 'base64')
}

module.exports = {
    encode: encode,
    decode: decode,
    decodeHex: decodeHex
}