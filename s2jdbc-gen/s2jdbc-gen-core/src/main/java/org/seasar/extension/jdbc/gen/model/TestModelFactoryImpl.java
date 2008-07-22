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
package org.seasar.extension.jdbc.gen.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.TestModel;
import org.seasar.extension.jdbc.gen.TestModelFactory;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link TestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class TestModelFactoryImpl implements TestModelFactory {

    /** 設定ファイルのパス */
    protected String configPath;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** テストクラス名のサフィックス */
    protected String ｔestClassNameSuffix;

    /**
     * インスタンスを構築します。
     * 
     * @param configPath
     *            設定ファイルのパス
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     * @param ｔestClassNameSuffix
     *            テストクラス名のサフィックス
     */
    public TestModelFactoryImpl(String configPath, String jdbcManagerName,
            String ｔestClassNameSuffix) {
        if (configPath == null) {
            throw new NullPointerException("configPath");
        }
        if (jdbcManagerName == null) {
            throw new NullPointerException("jdbcManagerName");
        }
        if (ｔestClassNameSuffix == null) {
            throw new NullPointerException("ｔestClassNameSuffix");
        }
        this.configPath = configPath;
        this.jdbcManagerName = jdbcManagerName;
        this.ｔestClassNameSuffix = ｔestClassNameSuffix;
    }

    public TestModel getEntityTestModel(EntityMeta entityMeta) {
        TestModel testModel = new TestModel();
        testModel.setConfigPath(configPath);
        testModel.setJdbcManagerName(jdbcManagerName);
        String packageName = ClassUtil.splitPackageAndShortClassName(entityMeta
                .getEntityClass().getName())[0];
        testModel.setPackageName(packageName);
        testModel.setShortClassName(entityMeta.getName() + ｔestClassNameSuffix);
        testModel.setShortEntityClassName(entityMeta.getName());
        doImportName(entityMeta, testModel);
        doIdValue(entityMeta, testModel);
        return testModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param testModel
     *            テストモデル
     */
    protected void doImportName(EntityMeta entityMeta, TestModel testModel) {
        testModel.addImportName(JdbcManager.class.getName());
        testModel.addImportName(S2TestCase.class.getName());
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            Class<?> propertyClass = propertyMeta.getPropertyClass();
            String name = ClassUtil.getPackageName(propertyClass);
            if (name != null && !"java.lang".equals(name)) {
                testModel.addImportName(propertyClass.getName());
            }
        }
    }

    /**
     * 識別子の式を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param testModel
     *            テストモデル
     */
    protected void doIdValue(EntityMeta entityMeta, TestModel testModel) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            Class<?> propertyClass = propertyMeta.getPropertyClass();
            testModel.addIdExpression(getExpression(propertyClass));
        }
    }

    /**
     * プロパティのクラスの値を表す式を取得します。
     * 
     * @param propertyClass
     *            プロパティのクラス
     * @return 識別子の式
     */
    protected String getExpression(Class<?> propertyClass) {
        Class<?> clazz = ClassUtil.getPrimitiveClassIfWrapper(propertyClass);
        if (clazz.isPrimitive()) {
            if (clazz == boolean.class) {
                return "true";
            }
            if (clazz == char.class) {
                return "'a'";
            }
            if (clazz == byte.class) {
                return "(byte) 1";
            }
            if (clazz == short.class) {
                return "(short) 1";
            }
            if (clazz == int.class) {
                return "1";
            }
            if (clazz == long.class) {
                return "1L";
            }
            if (clazz == float.class) {
                return "1f";
            }
            if (clazz == double.class) {
                return "1d";
            }
        }
        if (clazz == String.class) {
            return "\"aaa\"";
        }
        if (clazz == BigDecimal.class) {
            return "new BigDecimal(1)";
        }
        if (clazz == BigDecimal.class || clazz == BigInteger.class) {
            return "new BigInteger(1)";
        }
        if (clazz == Date.class) {
            return "new Date()";
        }
        if (clazz == Calendar.class) {
            return "Calendar.getInstance()";
        }
        if (clazz == java.sql.Date.class) {
            return "Date.valueOf(\"2008-01-01\")";
        }
        if (clazz == Timestamp.class) {
            return "Timestamp.valueOf(\"2008-01-01 12:00:00\")";
        }
        if (clazz == Time.class) {
            return "Time.valueOf(\"12:00:00\")";
        }
        throw new IllegalArgumentException("propertyClass");
    }
}