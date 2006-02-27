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
package org.seasar.framework.container.factory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author higa
 */
public class TigerAnnotationHandlerTest extends S2TestCase {

    private TigerAnnotationHandler handler = new TigerAnnotationHandler();

    public void testCreateComponentDef() throws Exception {
        assertNotNull("1", handler.createComponentDef(Hoge.class, null));
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        assertEquals("2", "aaa", cd.getComponentName());
        assertEquals("3", "prototype", cd.getInstanceDef().getName());
        assertEquals("4", "property", cd.getAutoBindingDef().getName());
        
        ComponentDef cd2 = handler.createComponentDef(Hoge.class, InstanceDefFactory.REQUEST);
        assertEquals("5", InstanceDef.REQUEST_NAME, cd2.getInstanceDef().getName());
        
        ComponentDef cd3 = handler.createComponentDef(Hoge7.class, null);
        assertEquals("6", "hoge77", cd3.getComponentName());
        assertEquals("7", InstanceDef.PROTOTYPE_NAME, cd3.getInstanceDef().getName());
        assertEquals("8", AutoBindingDef.NONE_NAME, cd3.getAutoBindingDef().getName());
        
        ComponentDef cd4 = handler.createComponentDef(Hoge8.class, null);
        assertEquals("9", "hoge7x", cd4.getComponentName());
        assertEquals("10", InstanceDef.SINGLETON_NAME, cd4.getInstanceDef().getName());
        assertEquals("11", AutoBindingDef.PROPERTY_NAME, cd4.getAutoBindingDef().getName());
        
        ComponentDef cd5 = handler.createComponentDef(Hoge7.class, InstanceDefFactory.REQUEST);
        assertEquals("12", InstanceDef.REQUEST_NAME, cd5.getInstanceDef().getName());
        
        ComponentDef cd6 = handler.createComponentDef(Hoge9.class, null);
        assertEquals("13", "hoge99", cd6.getComponentName());
        assertEquals("14", InstanceDef.PROTOTYPE_NAME, cd6.getInstanceDef().getName());
        assertEquals("15", AutoBindingDef.NONE_NAME, cd6.getAutoBindingDef().getName());
        
        ComponentDef cd7 = handler.createComponentDef(Hoge10.class, null);
        assertEquals("16", "hoge10x", cd7.getComponentName());
        assertEquals("17", InstanceDef.SINGLETON_NAME, cd7.getInstanceDef().getName());
        assertEquals("18", AutoBindingDef.PROPERTY_NAME, cd7.getAutoBindingDef().getName());
        
        ComponentDef cd8 = handler.createComponentDef(Hoge9.class, InstanceDefFactory.REQUEST);
        assertEquals("19", InstanceDef.REQUEST_NAME, cd8.getInstanceDef().getName());
    }

    public void testCreatePropertyDef() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertNull("1", handler.createPropertyDef(beanDesc, propDesc));

        beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        propDesc = beanDesc.getPropertyDesc("aaa");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("2", "aaa", propDef.getPropertyName());
        assertEquals("3", "aaa2", ((OgnlExpression) propDef.getExpression()).getSource());

        propDesc = beanDesc.getPropertyDesc("bbb");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", "none", propDef.getBindingTypeDef().getName());
    }
    
    public void testCreatePropertyDefForEJB3() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("ddd");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("1", "ddd", propDef.getPropertyName());
        assertNull("2", propDef.getExpression());
        
        propDesc = beanDesc.getPropertyDesc("eee");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("3", "eee2", ((OgnlExpression) propDef.getExpression()).getSource());
        
        propDesc = beanDesc.getPropertyDesc("iii");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", "ejb.iii", ((OgnlExpression) propDef.getExpression()).getSource());
    }
    
    public void testCreatePropertyDefForEJB3ForField() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        Field field = beanDesc.getField("fff");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("1", "fff", propDef.getPropertyName());
        assertNull("2", propDef.getExpression());
        
        field = beanDesc.getField("ggg");
        propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("3", "ggg2", ((OgnlExpression) propDef.getExpression()).getSource());
        
        field = beanDesc.getField("hhh");
        propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("3", "ejb.hhh", ((OgnlExpression) propDef.getExpression()).getSource());
    }
    
    public void testAppendDIForEJB3() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        handler.appendDI(cd);
        PropertyDef propDef = cd.getPropertyDef("ggg");
        assertEquals("1", "ggg2", ((OgnlExpression) propDef.getExpression()).getSource());
    }

    public void testCreatePropertyDefForConstantAnnotation() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertNull("1", handler.createPropertyDef(beanDesc, propDesc));

        beanDesc = BeanDescFactory.getBeanDesc(Hoge3.class);
        propDesc = beanDesc.getPropertyDesc("aaa");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("2", "aaa", propDef.getPropertyName());
        assertEquals("3", "aaa2", ((OgnlExpression) propDef.getExpression()).getSource());

        propDesc = beanDesc.getPropertyDesc("bbb");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", "none", propDef.getBindingTypeDef().getName());
    }

    public void testAppendAspect() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef.getExpression())
                .getSource());
    }

    public void testAppendAspectForMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge4.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef.getExpression())
                .getSource());
        assertTrue("3", aspectDef.getPointcut().isApplied(Hoge4.class.getMethod("getAaa")));
        assertFalse("4", aspectDef.getPointcut().isApplied(
                Hoge4.class.getMethod("getAaa", new Class[] { String.class })));
    }

    public void testAppendAspectForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef.getExpression())
                .getSource());
    }

    public void testAppendAspectForEJB3CMT1() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge11.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 6, cd.getAspectDefSize());
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < 6; ++i) {
            set.add(((OgnlExpression) cd.getAspectDef(i).getExpression()).getSource());
        }
        assertTrue("2", set.contains("ejbtx.mandatoryTx"));
        assertTrue("3", set.contains("ejbtx.requiredTx"));
        assertTrue("4", set.contains("ejbtx.requiresNewTx"));
        assertTrue("5", set.contains("ejbtx.notSupportedTx"));
        assertTrue("6", set.contains("ejbtx.neverTx"));
        assertTrue("7", set.contains("ejbtx.requiredTx"));
    }

    public void testAppendAspectForEJB3CMT2() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge12.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 2, cd.getAspectDefSize());
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < 2; ++i) {
            set.add(((OgnlExpression) cd.getAspectDef(i).getExpression()).getSource());
        }
        assertTrue("2", set.contains("ejbtx.mandatoryTx"));
        assertTrue("3", set.contains("ejbtx.requiredTx"));
    }

    public void testAppendAspectForEJB3BMT() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge13.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 0, cd.getAspectDefSize());
    }

    public void testAppendAspectForEJB3AroundInvoke() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge14.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 2, cd.getAspectDefSize());
        Hoge14 hoge14 = (Hoge14) cd.getComponent();
        assertEquals("2", "start-before-foo-after", hoge14.foo("start"));
        assertEquals("3", "start-before-bar-after", hoge14.bar("start"));
        assertEquals("4", "start-baz", hoge14.baz("start"));
    }

    public void testAppendAspectForEJB3AroundInvokeInvalid1() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge15.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 0, cd.getAspectDefSize());
    }

    public void testAppendAspectForEJB3AroundInvokeInvalid2() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge16.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 0, cd.getAspectDefSize());
    }

    public void testAppendAspectForEJB3AroundInvokeInvalid3() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge17.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 0, cd.getAspectDefSize());
    }

    public void testInterType() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInterType(cd);
        assertEquals("1", 1, cd.getInterTypeDefSize());
        InterTypeDef interTypeDef = cd.getInterTypeDef(0);
        assertEquals("2", "fieldInterType", ((OgnlExpression) interTypeDef.getExpression())
                .getSource());
    }

    public void testAppendInterTypeForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
        handler.appendInterType(cd);
        assertEquals("1", 1, cd.getInterTypeDefSize());
        InterTypeDef interTypeDef = cd.getInterTypeDef(0);
        assertEquals("2", "fieldInterType", ((OgnlExpression) interTypeDef.getExpression())
                .getSource());
    }

    public void testAppendInitMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInitMethod(cd);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("2", "init", initMethodDef.getMethodName());
    }

    public void testAppendInitMethodForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
        handler.appendInitMethod(cd);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("2", "init", initMethodDef.getMethodName());
    }

    public void setUpAppendInitMethodForDicon() throws Exception {
        include("TigerAnnotationHandlerTest.dicon");
    }

    public void testAppendInitMethodForDicon() throws Exception {
        ComponentDef cd = getComponentDef(Hoge5.class);
        assertEquals("1", 1, cd.getInitMethodDefSize());
    }

    public void testAppendInitMethodForException() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge6.class, null);
        try {
            handler.appendInitMethod(cd);
            fail("1");
        }
        catch (IllegalInitMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
    }
}