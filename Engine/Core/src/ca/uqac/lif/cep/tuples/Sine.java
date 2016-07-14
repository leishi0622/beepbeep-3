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

import java.util.Stack;

import ca.uqac.lif.cep.Connector.ConnectorException;

public class Sine extends UnaryExpression
{
	public Sine(AttributeExpression exp)
	{
		super("SIN", exp);
	}
	
	@Override
	public Object evaluate(Object t_left)
	{
		float n_left = EmlNumber.parseFloat(t_left);
		return Math.sin(n_left);
	}
	
	public static void build(Stack<Object> stack) throws ConnectorException
	{
		stack.pop(); // )
		AttributeExpression exp_left = (AttributeExpression) stack.pop();
		stack.pop(); // (
		stack.pop(); // SIN
		Sine op = new Sine(exp_left);
		stack.push(op);
	}
}
