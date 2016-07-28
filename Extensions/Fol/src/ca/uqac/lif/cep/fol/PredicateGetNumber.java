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
package ca.uqac.lif.cep.fol;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.numbers.NumberCast;

/**
 * Extracts the value at a specific position in the predicate tuple,
 * and converts it into a number
 */
public class PredicateGetNumber extends UnaryFunction<PredicateTuple,Number>
{
	protected int m_position;

	public PredicateGetNumber(int position)
	{
		super(PredicateTuple.class, Number.class);
		m_position = position;
	}

	@Override
	public Number getValue(PredicateTuple x) 
	{
		if (m_position == 0)
		{
			// This is the predicate's name!
			return 0;
		}
		if (m_position > x.m_arguments.size())
		{
			// > and not >=, as we use position - 1 below
			return -1;
		}
		return NumberCast.getNumber(x.getArgument(m_position - 1));
	}

	@Override
	public PredicateGetNumber clone()
	{
		return new PredicateGetNumber(m_position);
	}		
}