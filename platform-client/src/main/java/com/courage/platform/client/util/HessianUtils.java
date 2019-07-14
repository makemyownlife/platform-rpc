package com.courage.platform.client.util;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 编解码工具类
 * Created by zhangyong on 2019/7/10.
 */
public class HessianUtils {

    private static final Logger logger = LoggerFactory.getLogger(HessianUtils.class);

    public static byte[] encodeObject(final Object obj) throws IOException {
        ByteArrayOutputStream baos = null;
        Hessian2Output output = null;
        try {
            baos = new ByteArrayOutputStream();
            output = new Hessian2Output(baos);
            output.startCall();
            output.writeObject(obj);
            output.completeCall();
        } catch (final IOException ex) {
            throw ex;
        } finally {
            if (output != null) {
                try {
                    baos.close();
                } catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }

    public static byte[] encodeObject(final Object[] params) throws IOException {
        ByteArrayOutputStream baos = null;
        Hessian2Output output = null;
        try {
            baos = new ByteArrayOutputStream();
            output = new Hessian2Output(baos);
            output.startCall();
            for (final Object arg : params) {
                output.writeObject(arg);
            }
            output.completeCall();
        } catch (final IOException ex) {
            throw ex;
        } finally {
            if (output != null) {
                try {
                    baos.close();
                } catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }

    public static Object decodeObject(final byte[] in) throws IOException {
        Object obj = null;
        ByteArrayInputStream bais = null;
        Hessian2Input input = null;
        try {
            bais = new ByteArrayInputStream(in);
            input = new Hessian2Input(bais);
            input.startReply();
            obj = input.readObject();
            input.completeReply();
        } catch (final IOException ex) {
            throw ex;
        } catch (final Throwable e) {
            logger.error("Failed to decode object.", e);
        } finally {
            if (input != null) {
                try {
                    bais.close();
                } catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return obj;
    }

    public static Object[] decodeObject(final byte[] in, int count) throws IOException {
        Object[] params = new Object[count];
        ByteArrayInputStream bais = null;
        Hessian2Input input = null;
        try {
            bais = new ByteArrayInputStream(in);
            input = new Hessian2Input(bais);
            input.startReply();
            for (int i = 0; i < count; i++) {
                params[i] = input.readObject();
            }
            input.completeReply();
        } catch (final IOException ex) {
            throw ex;
        } catch (final Throwable e) {
            logger.error("Failed to decode object.", e);
        } finally {
            if (input != null) {
                try {
                    bais.close();
                } catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return params;
    }

}
