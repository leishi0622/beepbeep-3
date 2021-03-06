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
package ca.uqac.lif.cep.tmf;

import java.util.Queue;
import java.util.Stack;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.Processor;

/**
 * Returns the first <i>n</i> input events and discards the following ones.
 * 
 * @author Sylvain Hallé
 */
public class Prefix extends Trim
{	
	public Prefix(int k)
	{
		super(k);
	}
	
	@Override
	protected Queue<Object[]> compute(Object[] inputs)
	{
		m_eventsReceived++;
		if (m_eventsReceived <= m_delay)
		{
			return wrapVector(inputs);
		}
		return null;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_eventsReceived = 0;
	}
	
	public static void build(Stack<Object> stack) throws ConnectorException
	{
		stack.pop(); // (
		Processor p = (Processor) stack.pop();
		stack.pop(); // )
		stack.pop(); // OF
		Number interval = (Number) stack.pop();
		stack.pop(); // FIRST
		stack.pop(); // THE
		Prefix out = new Prefix(interval.intValue());
		Connector.connect(p, out);
		stack.push(out);
	}
}
