/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.beans.factory;

import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.impl.BeanDescImpl;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.MapUtil;

/**
 * {@link BeanDesc}を生成するクラスです。
 * 
 * @author higa
 * 
 */
public class BeanDescFactory {

    private static volatile boolean initialized;

    private static Map beanDescCache = MapUtil.createHashMap(1024);

    static {
        initialize();
    }

    /**
     * インスタンスを構築します。
     */
    protected BeanDescFactory() {
    }

    /**
     * {@link BeanDesc}を返します。
     * 
     * @param clazz
     * @return {@link BeanDesc}
     */
    public static BeanDesc getBeanDesc(Class clazz) {
        if (!initialized) {
            initialize();
        }
        BeanDesc beanDesc = (BeanDesc) beanDescCache.get(clazz);
        if (beanDesc == null) {
            beanDesc = new BeanDescImpl(clazz);
            beanDescCache.put(clazz, beanDesc);
        }
        return beanDesc;
    }

    /**
     * 初期化を行ないます。
     */
    public static void initialize() {
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    /**
     * キャッシュをクリアします。
     */
    public static void clear() {
        beanDescCache.clear();
        initialized = false;
    }
}
