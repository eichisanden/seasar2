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
package org.seasar.extension.tx.adapter;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.extension.tx.TransactionCallback;

import static javax.transaction.Status.*;

import static org.easymock.EasyMock.*;

/**
 * {@link JTATransactionManagerAdapterTest}のテストクラスです。
 * <p>
 * メソッド名は次のようになっています。
 * </p>
 * 
 * <pre>
 * test&lt;TXATTR&gt;_&lt;CONTEXT&gt;_&lt;BEHAVIOR&gt;()
 * </pre>
 * 
 * <dl>
 * <dt>&lt;TXATTR&gt;</dt>
 * <dd>テスト対象のトランザクション属性</dd>
 * <dt>&lt;CONTEXT&gt;</dt>
 * <dd>現在のスレッド上でトランザクションが開始されているか (<code>withTx</code>)いないか(<code>withoutTx()</code></dd>
 * <dt>&lt;BEHAVIOR&gt;</dt>
 * <dd>{@link TransactionCallback}の振る舞い。 リターンしてコミット (<code>returnCommit</code>)、
 * リターンしてロールバック (<code>returnRollback)、
 * 例外をスローしてコミット (<code>exceptionCommit</code>)、 例外をスローしてロールバック (<code>exceptionRollback)
 * のいすれか</dd>
 * </dl>
 * 
 * @author koichik
 */
public class JTATransactionManagerAdapterTest extends
        AbstractTransactionManagerAdapterTest {

    JTATransactionManagerAdapter target;

    TransactionManager tm;

    Transaction tx;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tm = createStrictMock(TransactionManager.class);
        tx = createStrictMock(Transaction.class);
        target = new JTATransactionManagerAdapter(tm);
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withoutTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.required(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withoutTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.commit();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withoutTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.required(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withoutTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
        expect(tm.getStatus()).andReturn(STATUS_MARKED_ROLLBACK);
        tm.rollback();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withoutTx_exceptionCommit() throws Throwable {
        try {
            target.required(new ExceptionCommitCallback());
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withoutTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.commit();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withoutTx_exceptionRollback() throws Throwable {
        try {
            target.required(new ExceptionRollbackCallback());
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withoutTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
        expect(tm.getStatus()).andReturn(STATUS_MARKED_ROLLBACK);
        tm.rollback();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.required(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.required(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target.required(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testRequired_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .required(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_withTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withoutTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.requiresNew(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withoutTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.commit();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withoutTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.requiresNew(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withoutTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
        expect(tm.getStatus()).andReturn(STATUS_MARKED_ROLLBACK);
        tm.rollback();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withoutTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .requiresNew(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withoutTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.commit();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withoutTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .requiresNew(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withoutTx_exceptionRollback()
            throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
        expect(tm.getStatus()).andReturn(STATUS_MARKED_ROLLBACK);
        tm.rollback();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.requiresNew(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.commit();
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.requiresNew(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
        expect(tm.getStatus()).andReturn(STATUS_MARKED_ROLLBACK);
        tm.rollback();
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .requiresNew(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.commit();
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .requiresNew(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_withTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.begin();
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
        expect(tm.getStatus()).andReturn(STATUS_MARKED_ROLLBACK);
        tm.rollback();
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_returnCommit() throws Throwable {
        try {
            assertEquals("hoge", target.mandatory(new ReturnCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_returnRollback() throws Throwable {
        try {
            assertEquals("hoge", target.mandatory(new ReturnRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.mandatory(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.mandatory(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        tm.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withoutTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.notSupported(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withoutTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withoutTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.notSupported(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withoutTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withoutTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .notSupported(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withoutTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withoutTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .notSupported(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withoutTx_exceptionRollback()
            throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.notSupported(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.notSupported(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .notSupported(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .notSupported(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_withTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
        expect(tm.suspend()).andReturn(tx);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        tm.resume(tx);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.never(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.never(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
        expect(tm.getStatus()).andReturn(STATUS_NO_TRANSACTION);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_returnCommit() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ReturnCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_returnCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_returnRollback() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ReturnRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_returnRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_exceptionCommit() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_exceptionRollback() throws Throwable {
        expect(tm.getStatus()).andReturn(STATUS_ACTIVE);
    }

}