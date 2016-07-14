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
package ca.uqac.lif.cep;

import java.util.Queue;

/**
 * Returns the same event over and over when pulled
 * 
 * @author Sylvain Hallé
 */
public class PullConstant extends SingleProcessor
{
	/**
	 * The event to return
	 */
	private final Object m_toReturn;

	/**
	 * Creates a constant processor
	 * @param o The event to return
	 */
	public PullConstant(Object o)
	{
		super(0, 1);
		m_toReturn = o;
	}

	@Override
	protected Queue<Object[]> compute(Object[] inputs)
	{
		return wrapObject(m_toReturn);
	}

	@Override
	public PullConstant clone() 
	{
		return new PullConstant(m_toReturn);
	}
}
