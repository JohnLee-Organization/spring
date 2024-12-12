/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-11
 * @time : 08:15
 */
package net.lizhaoweb.quentin;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.X509Certificate;

/**
 * A <b>Realm</b> is a read-only facade for an underlying security realm
 * used to authenticate individual users, and identify the security roles
 * associated with those users.  Realms can be attached at any Container
 * level, but will typically only be attached to a Context, or higher level,
 * Container.
 * <p>
 * Created by jhon on 2024/6/11 8:15
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface Realm {


    // ------------------------------------------------------------- Properties


    /**
     * Return the Container with which this Realm has been associated.
     */
    Container getContainer();


    /**
     * Set the Container with which this Realm has been associated.
     *
     * @param container The associated Container
     */
    void setContainer(Container container);


    /**
     * Return descriptive information about this Realm implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    String getInfo();


    // --------------------------------------------------------- Public Methods


    /**
     * Add a property change listener to this component.
     *
     * @param listener The listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener);


    /**
     * Return the Principal associated with the specified username and
     * credentials, if there is one; otherwise return <code>null</code>.
     *
     * @param username    Username of the Principal to look up
     * @param credentials Password or other credentials to use in
     *                    authenticating this username
     */
    Principal authenticate(String username, String credentials);


    /**
     * Return the Principal associated with the specified username, which
     * matches the digest calculated using the given parameters using the
     * method described in RFC 2069; otherwise return <code>null</code>.
     *
     * @param username Username of the Principal to look up
     * @param digest   Digest which has been submitted by the client
     * @param nonce    Unique (or supposedly unique) token which has been used
     *                 for this request
     * @param realm    Realm name
     * @param md5a2    Second MD5 digest used to calculate the digest :
     *                 MD5(Method + ":" + uri)
     */
    Principal authenticate(String username, String digest, String nonce, String nc, String cnonce, String qop, String realm, String md5a2);


    /**
     * Return the Principal associated with the specified chain of X509
     * client certificates.  If there is none, return <code>null</code>.
     *
     * @param certs Array of client certificates, with the first one in
     *              the array being the certificate of the client itself.
     */
    Principal authenticate(X509Certificate[] certs);


    /**
     * Execute a periodic task, such as reloading, etc. This method will be
     * invoked inside the classloading context of this container. Unexpected
     * throwables will be caught and logged.
     */
    void backgroundProcess();


    /**
     * Return the SecurityConstraints configured to guard the request URI for
     * this request, or <code>null</code> if there is no such constraint.
     *
     * @param request Request we are processing
     */
    SecurityConstraint[] findSecurityConstraints(Request request, Context context);


    /**
     * Perform access control based on the specified authorization constraint.
     * Return <code>true</code> if this constraint is satisfied and processing
     * should continue, or <code>false</code> otherwise.
     *
     * @param request    Request we are processing
     * @param response   Response we are creating
     * @param constraint Security constraint we are enforcing
     * @param context    The Context to which client of this class is attached.
     * @throws IOException if an input/output error occurs
     */
    boolean hasResourcePermission(Request request, Response response, SecurityConstraint[] constraint, Context context) throws IOException;


    /**
     * Return <code>true</code> if the specified Principal has the specified
     * security role, within the context of this Realm; otherwise return
     * <code>false</code>.
     *
     * @param principal Principal for whom the role is to be checked
     * @param role      Security role to be checked
     */
    boolean hasRole(Principal principal, String role);

    /**
     * Enforce any user data constraint required by the security constraint
     * guarding this request URI.  Return <code>true</code> if this constraint
     * was not violated and processing should continue, or <code>false</code>
     * if we have created a response already.
     *
     * @param request    Request we are processing
     * @param response   Response we are creating
     * @param constraint Security constraint being checked
     * @throws IOException if an input/output error occurs
     */
    boolean hasUserDataPermission(Request request, Response response, SecurityConstraint[] constraint) throws IOException;

    /**
     * Remove a property change listener from this component.
     *
     * @param listener The listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
