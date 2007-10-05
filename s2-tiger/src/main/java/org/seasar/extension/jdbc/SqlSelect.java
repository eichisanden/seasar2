/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc;

/**
 * SQLを直接指定する検索です。
 * 
 * @author higa
 * @param <T>
 *            戻り値のベースの型です。
 * 
 */
public interface SqlSelect<T> extends Select<T> {

    /**
     * 検索を呼び出すクラスを設定します。デフォルトは {@link SqlSelect}の実装クラスです。
     * 
     * @param callerClass
     *            検索を呼び出すクラス
     * @return SQL検索
     */
    SqlSelect<T> callerClass(Class<?> callerClass);

    /**
     * 検索を呼び出すメソッド名を設定します。デフォルトはgetResultListあるいはgetSingleResultになります。
     * 
     * @param callerMethodName
     *            検索を呼び出すメソッド名
     * @return SQL検索
     */
    SqlSelect<T> callerMethodName(String callerMethodName);

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     * @return SQL検索
     */
    SqlSelect<T> maxRows(int maxRows);

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     * @return SQL検索
     */
    SqlSelect<T> fetchSize(int fetchSize);

    /**
     * クエリタイムアウトの秒数を設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウトの秒数
     * @return SQL検索
     */
    SqlSelect<T> queryTimeout(int queryTimeout);

    /**
     * リミットを設定します。
     * 
     * @param limit
     *            リミット
     * @return SQL検索
     */
    SqlSelect<T> limit(int limit);

    /**
     * オフセットを設定します。
     * 
     * @param offset
     *            オフセット
     * @return SQL検索
     */
    SqlSelect<T> offset(int offset);
}