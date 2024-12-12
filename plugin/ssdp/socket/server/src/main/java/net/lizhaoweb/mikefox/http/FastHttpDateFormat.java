/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.http
 * @date : 2024-06-07
 * @time : 17:28
 */
package net.lizhaoweb.mikefox.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class to generate HTTP dates.
 * <p>
 * Created by jhon on 2024/6/7 17:28
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public final class FastHttpDateFormat {


    // -------------------------------------------------------------- Variables


    private static final int CACHE_SIZE = Integer.parseInt(System.getProperty("org.apache.tomcat.util.http.FastHttpDateFormat.CACHE_SIZE", "1000"));


    /**
     * HTTP date format.
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);


    /**
     * The set of SimpleDateFormat formats to use in getDateHeader().
     */
    private static final SimpleDateFormat[] formats = { //
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US), //
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US), //
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US) //
    };


    private final static TimeZone gmtZone = TimeZone.getTimeZone("GMT");


    /*
     * GMT timezone - all HTTP dates are on GMT
     */
    static {
        format.setTimeZone(gmtZone);
        formats[0].setTimeZone(gmtZone);
        formats[1].setTimeZone(gmtZone);
        formats[2].setTimeZone(gmtZone);
    }


    /**
     * Instant on which the currentDate object was generated.
     */
    private static long currentDateGenerated = 0L;


    /**
     * Current formatted date.
     */
    private static String currentDate = null;


    /**
     * Formatter cache.
     */
    private static final ConcurrentHashMap<Long, String> formatCache = new ConcurrentHashMap<Long, String>(CACHE_SIZE);


    /**
     * Parser cache.
     */
    private static final ConcurrentHashMap<String, Long> parseCache = new ConcurrentHashMap<String, Long>(CACHE_SIZE);


    // --------------------------------------------------------- Public Methods


    /**
     * Get the current date in HTTP format.
     */
    public static final String getCurrentDate() {
        long now = System.currentTimeMillis();
        if ((now - currentDateGenerated) > 1000) {
            synchronized (format) {
                if ((now - currentDateGenerated) > 1000) {
                    currentDateGenerated = now;
                    currentDate = format.format(new Date(now));
                }
            }
        }
        return currentDate;
    }


    /**
     * Get the HTTP format of the specified date.
     */
    public static final String formatDate(long value, DateFormat threadLocalformat) {
        Long longValue = value;
        String cachedDate = formatCache.get(longValue);
        if (cachedDate != null) return cachedDate;
        String newDate = null;
        Date dateValue = new Date(value);
        if (threadLocalformat != null) {
            newDate = threadLocalformat.format(dateValue);
            updateFormatCache(longValue, newDate);
        } else {
            synchronized (formatCache) {
                synchronized (format) {
                    newDate = format.format(dateValue);
                }
                updateFormatCache(longValue, newDate);
            }
        }
        return newDate;
    }


    /**
     * Try to parse the given date as a HTTP date.
     */
    public static final long parseDate(String value, DateFormat[] threadLocalformats) {
        Long cachedDate = parseCache.get(value);
        if (cachedDate != null) return cachedDate;
        Long date = null;
        if (threadLocalformats != null) {
            date = internalParseDate(value, threadLocalformats);
            updateParseCache(value, date);
        } else {
            synchronized (parseCache) {
                date = internalParseDate(value, formats);
                updateParseCache(value, date);
            }
        }
        if (date == null) {
            return (-1L);
        }
        return date;
    }


    /**
     * Parse date with given formatters.
     */
    private static final Long internalParseDate(String value, DateFormat[] formats) {
        Date date = null;
        for (int i = 0; (date == null) && (i < formats.length); i++) {
            try {
                date = formats[i].parse(value);
            } catch (ParseException e) {
                // Ignore
            }
        }
        if (date == null) {
            return null;
        }
        return date.getTime();
    }


    /**
     * Update cache.
     */
    private static void updateFormatCache(Long key, String value) {
        if (value == null) {
            return;
        }
        if (formatCache.size() > CACHE_SIZE) {
            formatCache.clear();
        }
        formatCache.put(key, value);
    }


    /**
     * Update cache.
     */
    private static void updateParseCache(String key, Long value) {
        if (value == null) {
            return;
        }
        if (parseCache.size() > CACHE_SIZE) {
            parseCache.clear();
        }
        parseCache.put(key, value);
    }

}
