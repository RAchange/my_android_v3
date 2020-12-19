package com.example.myapplicationrecycle_view.Protocol;

import com.example.myapplicationrecycle_view.Crypto.AESCoder;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataServlet extends HttpServlet {
    private static final long serialVersionUID = -6219906900195793155L;
    private static String key;
    private static final String KEY_PARAM = "key";
    private static final String HEAD_MD = "messageDigest";

    @Override
    public void init() throws ServletException {
        super.init();
        key = getInitParameter(KEY_PARAM);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String messageDigest = request.getHeader(HEAD_MD);
            byte[] input = HttpUtils.requestRead(request);
            byte[] data = AESCoder.decrypt(input, key);
            System.err.println(new String(data));
            byte[] output = "".getBytes();
            if (AESCoder.validate(data, messageDigest)) {
                output = "OK".getBytes();
            }
            HttpUtils.responseWrite(response, AESCoder.encrypt(output, key));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
