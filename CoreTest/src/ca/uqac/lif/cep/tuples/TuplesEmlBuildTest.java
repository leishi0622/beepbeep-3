/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.tuples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cep.BeepBeepUnitTest;
import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Palette;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.interpreter.Interpreter;
import ca.uqac.lif.cep.interpreter.Interpreter.ParseException;
import ca.uqac.lif.cep.io.StreamReader;
import ca.uqac.lif.cep.util.StringUtils;
import ca.uqac.lif.util.PackageFileReader;

public class TuplesEmlBuildTest extends BeepBeepUnitTest
{
	protected Interpreter m_interpreter;

	@Before
	public void setUp()
	{
		m_interpreter = new Interpreter();		
		Palette ext = new TupleGrammar();
		m_interpreter.extendGrammar(ext);
	}
	
	@Test
	public void testProcList1() throws ParseException, ConnectorException
	{
		String expression = "0";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(1, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList2() throws ParseException, ConnectorException
	{
		String expression = "0, 1";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(2, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList3() throws ParseException, ConnectorException
	{
		String expression = "0 AS matrace";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(1, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList3b() throws ParseException, ConnectorException
	{
		String expression = "(0) AS matrace";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertNotNull(result);
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(1, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList4() throws ParseException, ConnectorException
	{
		String expression = "0 AS matrace, 1 AS matrace";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertNotNull(result);
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(2, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testAttributeExpression1() throws ParseException, ConnectorException
	{
		String expression = "0";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof AttributeExpression);
	}
	
	@Test
	public void testAttributeExpression2() throws ParseException, ConnectorException
	{
		String expression = "0";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof NumberExpression);
	}
	
	@Test
	public void testAttributeExpression3() throws ParseException, ConnectorException
	{
		String expression = "(0) + (0)";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof Addition);
	}
	
	@Test
	public void testAttributeExpression4() throws ParseException, ConnectorException
	{
		String expression = "t.p";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof AttributeNameQualified);
	}
	
	@Test
	public void testAttributeList1() throws ParseException, ConnectorException
	{
		String expression = "t.p, t.p";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_list>");
		assertTrue(result instanceof AttributeList);
	}
	
	@Test
	public void testAttributeList2() throws ParseException, ConnectorException
	{
		String expression = "t.p, t.p AS q";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_list>");
		assertTrue(result instanceof AttributeList);
	}
	
	@Test
	public void testAttributeList3() throws ParseException, ConnectorException
	{
		String expression = "(t.p) + (2) AS w, t.p AS q";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_list>");
		assertTrue(result instanceof AttributeList);
	}
	
	@Test
	public void testSelect1() throws ParseException, ConnectorException
	{
		String expression = "SELECT (t.p) + (2) AS w, t.p AS q FROM (0)";
		Object result = m_interpreter.parseLanguage(expression, "<eml_select>");
		assertTrue(result instanceof Select);
	}
	
	@Test
	public void testSelect2() throws ParseException, ConnectorException
	{
		String expression = "SELECT (a) LESS THAN (0) FROM (0)";
		Object result = m_interpreter.parseLanguage(expression, "<eml_select>");
		assertTrue(result instanceof Select);
	}
	
	@Test
	public void testTupleFeeder1() throws FileNotFoundException, ConnectorException
	{
		String file_contents = PackageFileReader.readPackageFile(this.getClass(), "resource/tuples2.csv");
		InputStream stream = StringUtils.toInputStream(file_contents);
		StreamReader sr = new StreamReader(stream);
		TupleFeeder tf = new TupleFeeder();
		Connector.connect(sr, tf);
		Pullable p = tf.getPullableOutput(0);
		NamedTuple t = null;
		t = (NamedTuple) p.pull();
		assertNotNull(t);
		assertEquals("1", t.get("a").toString());
		t = (NamedTuple) p.pull();
		assertNotNull(t);
		assertEquals("2", t.get("a").toString());
		t = (NamedTuple) p.pull();
		assertNotNull(t);
		assertEquals("3", t.get("a").toString());
		t = (NamedTuple) p.pull();
		assertNotNull(t);
		assertEquals("10", t.get("a").toString());
	}
}
