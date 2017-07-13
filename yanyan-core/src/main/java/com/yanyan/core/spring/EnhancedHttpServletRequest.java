package com.yanyan.core.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * User: Saintcy
 * Date: 2015/4/10
 * Time: 13:25
 */
public class EnhancedHttpServletRequest extends HttpServletRequestWrapper {
    private Map<String, String[]> parameters = new HashMap<String, String[]>();

    public EnhancedHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value;
        String[] values = parameters.get(name);
        if (values != null) {
            value = parameters.get(name)[0];
        } else {
            value = super.getParameter(name);
        }

        return value;
    }

    public void setParameter(String name, String value) {
        parameters.put(name, new String[]{value});
    }

    public void setParameters(String name, String[] values) {
        parameters.put(name, values);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        if (map == null) {
            map = new HashMap<String, String[]>();
        }
        map.putAll(parameters);
        return map;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        List<String> names = new ArrayList<String>();
        Enumeration<String> enumeration = super.getParameterNames();
        while (enumeration.hasMoreElements()) {
            names.add(enumeration.nextElement());
        }
        for (String key : parameters.keySet()) {
            names.add(key);
        }
        return Collections.enumeration(names);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = parameters.get(name);
        if (values == null) {
            values = super.getParameterValues(name);
        }

        return values;
    }
}
