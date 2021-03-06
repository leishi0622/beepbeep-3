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

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.SingleProcessor;

/**
 * Discards events from an input trace based on a selection criterion.
 * The processor takes as input two events simultaneously; it outputs
 * the first if the second is true.
 * 
 * @author Sylvain Hallé
 */
public class Filter extends SingleProcessor
{
	public Filter()
	{
		super(2, 1);
	}

	@Override
	protected Queue<Object[]> compute(Object[] inputs)
	{
		Object o = inputs[0];
		Object[] out = new Object[1];
		boolean b = (Boolean) inputs[inputs.length - 1];
		if (b)
		{
			out[0] = o;
		}
		else
		{
			// Don't output null, but rather an empty queue
			return new ArrayDeque<Object[]>();
		}
		return wrapVector(out);
	}

	public static void build(Stack<Object> stack) throws ConnectorException
	{
		// TODO
	}

	@Override
	public Filter clone() 
	{
		return new Filter();
	}
}
