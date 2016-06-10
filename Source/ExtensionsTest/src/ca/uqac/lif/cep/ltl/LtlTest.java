/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2015 Sylvain Hallé

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
package ca.uqac.lif.cep.ltl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.epl.QueueSource;
import ca.uqac.lif.cep.interpreter.InterpreterTestFrontEnd;
import ca.uqac.lif.cep.interpreter.Interpreter.ParseException;
import ca.uqac.lif.cep.io.StreamGrammar;
import ca.uqac.lif.cep.numbers.NumberGrammar;
import ca.uqac.lif.cep.tuples.TupleGrammar;
import ca.uqac.lif.cep.ltl.Troolean.Value;

/**
 * Unit tests for the LTL operators
 * @author Sylvain Hallé
 */
public class LtlTest 
{
	protected InterpreterTestFrontEnd m_interpreter;

	@Before
	public void setUp()
	{
		m_interpreter = new InterpreterTestFrontEnd();
		m_interpreter.extendGrammar(NumberGrammar.class);
		m_interpreter.extendGrammar(StreamGrammar.class);
		m_interpreter.extendGrammar(TupleGrammar.class);
		m_interpreter.extendGrammar(LtlGrammar.class);
	}
	
	@Test
	public void testGlobally1()
	{
		QueueSource src = new QueueSource(null, 1);
		Vector<Object> input_events = new Vector<Object>();
		input_events.add(Value.TRUE);
		input_events.add(Value.TRUE);
		input_events.add(Value.FALSE);
		input_events.add(Value.TRUE);
		src.setEvents(input_events);
		Globally g = new Globally();
		Connector.connect(src, g);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
	}
	
	@Test
	public void testEventually1()
	{
		QueueSource src = new QueueSource(null, 1);
		Vector<Object> input_events = new Vector<Object>();
		input_events.add(Value.FALSE);
		input_events.add(Value.FALSE);
		input_events.add(Value.TRUE);
		input_events.add(Value.FALSE);
		src.setEvents(input_events);
		Eventually g = new Eventually();
		Connector.connect(src, g);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertNotNull(b);
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testNext1()
	{
		QueueSource src = new QueueSource(null, 1);
		Vector<Object> input_events = new Vector<Object>();
		input_events.add(Value.FALSE);
		input_events.add(Value.FALSE);
		input_events.add(Value.TRUE);
		input_events.add(Value.FALSE);
		src.setEvents(input_events);
		Next g = new Next();
		Connector.connect(src, g);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertNotNull(b);
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testNext2()
	{
		QueueSource src = new QueueSource(null, 1);
		Vector<Object> input_events = new Vector<Object>();
		input_events.add(Value.FALSE);
		input_events.add(Value.TRUE);
		input_events.add(Value.TRUE);
		input_events.add(Value.FALSE);
		src.setEvents(input_events);
		Next g = new Next();
		Connector.connect(src, g);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertNotNull(b);
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testNot()
	{
		QueueSource src = new QueueSource(null, 1);
		Vector<Object> input_events = new Vector<Object>();
		input_events.add(Value.FALSE);
		input_events.add(Value.TRUE);
		input_events.add(Value.TRUE);
		input_events.add(Value.FALSE);
		src.setEvents(input_events);
		Not g = new Not();
		Connector.connect(src, g);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testAnd1()
	{
		QueueSource src_left = new QueueSource(null, 1);
		QueueSource src_right = new QueueSource(null, 1);
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src_left.setEvents(input_events);
		}
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			src_right.setEvents(input_events);
		}
		And g = new And();
		Connector.connect(src_left, g, 0, 0);
		Connector.connect(src_right, g, 0, 1);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
	}
	
	@Test
	public void testAnd2()
	{
		QueueSource src_left = new QueueSource(null, 1);
		QueueSource src_right = new QueueSource(null, 1);
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			src_left.setEvents(input_events);
		}
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(null);
			input_events.add(Value.TRUE);
			src_right.setEvents(input_events);
		}
		And g = new And();
		Connector.connect(src_left, g, 0, 0);
		Connector.connect(src_right, g, 0, 1);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
	}
	
	@Test
	public void testOr()
	{
		QueueSource src_left = new QueueSource(null, 1);
		QueueSource src_right = new QueueSource(null, 1);
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src_left.setEvents(input_events);
		}
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			src_right.setEvents(input_events);
		}
		Or g = new Or();
		Connector.connect(src_left, g, 0, 0);
		Connector.connect(src_right, g, 0, 1);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testUntil1()
	{
		QueueSource src_left = new QueueSource(null, 1);
		QueueSource src_right = new QueueSource(null, 1);
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src_left.setEvents(input_events);
		}
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			src_right.setEvents(input_events);
		}
		Until g = new Until();
		Connector.connect(src_left, g, 0, 0);
		Connector.connect(src_right, g, 0, 1);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
	}
	
	@Test
	public void testUntil2()
	{
		QueueSource src_left = new QueueSource(null, 1);
		QueueSource src_right = new QueueSource(null, 1);
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src_left.setEvents(input_events);
		}
		{
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src_right.setEvents(input_events);
		}
		Until g = new Until();
		Connector.connect(src_left, g, 0, 0);
		Connector.connect(src_right, g, 0, 1);
		Pullable p = g.getPullableOutput(0);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testExpression1()
	{
		String expression = "(@T) AND (@U)";
		{
			QueueSource src = new QueueSource(null, 1);
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src.setEvents(input_events);
			m_interpreter.addPlaceholder("@T", "processor", src);
		}
		{
			QueueSource src = new QueueSource(null, 1);
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src.setEvents(input_events);
			m_interpreter.addPlaceholder("@U", "processor", src);
		}
		Pullable p = m_interpreter.executeQuery(expression);
		Value b;
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
		b = (Value) p.pull();
		assertEquals(Value.FALSE, b);
	}
	
	@Test
	public void testExpression2()
	{
		String expression = "(@T) AND (X (@U))";
		{
			QueueSource src = new QueueSource(null, 1);
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src.setEvents(input_events);
			m_interpreter.addPlaceholder("@T", "processor", src);
		}
		{
			QueueSource src = new QueueSource(null, 1);
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src.setEvents(input_events);
			m_interpreter.addPlaceholder("@U", "processor", src);
		}
		Pullable p = m_interpreter.executeQuery(expression);
		assertNotNull(p);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testExpression3()
	{
		String expression = "X (@U)";
		{
			QueueSource src = new QueueSource(null, 1);
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src.setEvents(input_events);
			m_interpreter.addPlaceholder("@U", "processor", src);
		}
		Pullable p = m_interpreter.executeQuery(expression);
		assertNotNull(p);
		Value b;
		b = (Value) p.pull();
		assertNull(b);
		b = (Value) p.pull();
		assertEquals(Value.TRUE, b);
	}
	
	@Test
	public void testMultiline()
	{
		String expression = "(SELECT (a) LESS THAN (2) FROM (@P))\nAND\n(X (SELECT (a) GREATER THAN (1) FROM (@P)))";
		{
			QueueSource src = new QueueSource(null, 1);
			Vector<Object> input_events = new Vector<Object>();
			input_events.add(Value.FALSE);
			input_events.add(Value.TRUE);
			input_events.add(Value.TRUE);
			input_events.add(Value.FALSE);
			src.setEvents(input_events);
			m_interpreter.addPlaceholder("@P", "processor", src);
		}
		Pullable p = m_interpreter.executeQuery(expression);
		assertNotNull(p);
	}
	
	@Test
	public void testMultipleQueries1() throws ParseException, IOException
	{
		InputStream is = this.getClass().getResourceAsStream("test.esql");
		Object o = m_interpreter.executeQueries(is);
		assertNotNull(o);
	}
	
	@Test
	public void testMultipleQueries2() throws ParseException, IOException
	{
		InputStream is = this.getClass().getResourceAsStream("test2.esql");
		m_interpreter.executeQueries(is);
	}
	
	@Test
	public void testMultipleQueries3() throws ParseException, IOException
	{
		InputStream is = this.getClass().getResourceAsStream("test3.esql");
		Object o = m_interpreter.executeQueries(is);
		assertNotNull(o);
	}
	
}