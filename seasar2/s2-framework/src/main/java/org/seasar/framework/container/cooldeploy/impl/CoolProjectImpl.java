/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.container.cooldeploy.impl;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.cooldeploy.CoolCreator;
import org.seasar.framework.container.cooldeploy.CoolProject;

/**
 * @author higa
 * 
 */
public class CoolProjectImpl implements CoolProject {

    private String rootPackageName;

    private List ignorePackageNames = new ArrayList();

    private CoolCreator[] creators = new CoolCreator[0];

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public List getIgnorePackageNames() {
        return ignorePackageNames;
    }

    public void addIgnorePackageName(String packageName) {
        ignorePackageNames.add(packageName);
    }

    public CoolCreator[] getCreators() {
        return creators;
    }

    public void setCreators(CoolCreator[] creators) {
        this.creators = creators;
    }

    public boolean loadComponentDef(Class clazz) {
        if (rootPackageName != null
                && !clazz.getName().startsWith(rootPackageName)) {
            return false;
        }
        for (int i = 0; i < creators.length; ++i) {
            CoolCreator creator = creators[i];
            if (creator.loadComponentDef(rootPackageName, clazz)) {
                return true;
            }
        }
        return false;
    }

    public int matchClassName(String className) {
        if (rootPackageName != null && !className.startsWith(rootPackageName)) {
            return UNMATCH;
        }
        String base = rootPackageName == null ? "" : rootPackageName + ".";
        for (int i = 0; i < ignorePackageNames.size(); ++i) {
            if (className.startsWith(base + ignorePackageNames.get(i))) {
                return IGNORE;
            }
        }
        return MATCH;
    }
}