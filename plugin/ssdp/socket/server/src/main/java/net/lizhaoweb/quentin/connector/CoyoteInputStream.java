/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin.connector
 * @date : 2024-06-11
 * @time : 09:42
 */
package net.lizhaoweb.quentin.connector;

import net.lizhaoweb.quentin.security.SecurityUtil;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * This class handles reading bytes.
 * <p>
 * Created by jhon on 2024/6/11 9:42
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class CoyoteInputStream extends ServletInputStream {


    // ----------------------------------------------------- Instance Variables


    protected InputBuffer ib;


    // ----------------------------------------------------------- Constructors


    protected CoyoteInputStream(InputBuffer ib) {
        this.ib = ib;
    }


    // -------------------------------------------------------- Package Methods


    /**
     * Clear facade.
     */
    void clear() {
        ib = null;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Prevent cloning the facade.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    // --------------------------------------------- ServletInputStream Methods


    @Override
    public int read() throws IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() {

                    public Integer run() throws IOException {
                        return ib.readByte();
                    }

                });
                return result;
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            return ib.readByte();
        }
    }

    @Override
    public int available() throws IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() {

                    public Integer run() throws IOException {
                        return ib.available();
                    }

                });
                return result;
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            return ib.available();
        }
    }

    @Override
    public int read(final byte[] b) throws IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() {

                    public Integer run() throws IOException {
                        return ib.read(b, 0, b.length);
                    }

                });
                return result;
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            return ib.read(b, 0, b.length);
        }
    }


    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                Integer result = AccessController.doPrivileged(new PrivilegedExceptionAction<Integer>() {

                    public Integer run() throws IOException {
                        return ib.read(b, off, len);
                    }

                });
                return result;
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            return ib.read(b, off, len);
        }
    }


    @Override
    public int readLine(byte[] b, int off, int len) throws IOException {
        return super.readLine(b, off, len);
    }


    /**
     * Close the stream
     * Since we re-cycle, we can't allow the call to super.close()
     * which would permanently disable us.
     */
    @Override
    public void close() throws IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {

                    public Void run() throws IOException {
                        ib.close();
                        return null;
                    }

                });
            } catch (PrivilegedActionException pae) {
                Exception e = pae.getException();
                if (e instanceof IOException) {
                    throw (IOException) e;
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            ib.close();
        }
    }

}
