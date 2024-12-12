/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.modeler.modules
 * @date : 2024-06-07
 * @time : 09:08
 */
package net.lizhaoweb.mikefox.modeler.modules;

import net.lizhaoweb.mikefox.modeler.Registry;

import javax.management.ObjectName;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Source for descriptor data. More sources can be added.
 * <p>
 * Created by jhon on 2024/6/7 9:08
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class ModelerSource {

    protected Object source;
    protected String location;

    /**
     * Load data, returns a list of items.
     *
     * @param registry registry
     * @param location location
     * @param type     type
     * @param source   Introspected object or some other source
     * @throws Exception Exception
     */
    public List<ObjectName> loadDescriptors(Registry registry, String location, String type, Object source) throws Exception {
        // TODO
        return null;
    }

    /**
     * Callback from the BaseMBean to notify that an attribute has changed.
     * Can be used to implement persistence.
     *
     * @param oname ObjectName
     * @param name  name
     * @param value value
     */
    public void updateField(ObjectName oname, String name, Object value) {
        // nothing by default
    }

    public void store() {
        // nothing
    }

    protected InputStream getInputStream() throws IOException {
        if (source instanceof URL) {
            URL url = (URL) source;
            location = url.toString();
            return url.openStream();
        } else if (source instanceof File) {
            location = ((File) source).getAbsolutePath();
            return Files.newInputStream(((File) source).toPath());
        } else if (source instanceof String) {
            location = (String) source;
            return Files.newInputStream(Paths.get((String) source));
        } else if (source instanceof InputStream) {
            return (InputStream) source;
        }
        return null;
    }

}
