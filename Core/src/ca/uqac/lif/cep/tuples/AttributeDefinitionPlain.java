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

public class AttributeDefinitionPlain extends AttributeDefinitionAs
{
	public AttributeDefinitionPlain(AttributeExpression aexp)
	{
		super(aexp, "");
	}
	
	public static void build(Stack<Object> stack) throws ConnectorException
	{
		AttributeExpression exp = (AttributeExpression) stack.pop();
		AttributeDefinitionPlain adp = new AttributeDefinitionPlain(exp);
		stack.push(adp);
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_expression);
		return out.toString();
	}
}
