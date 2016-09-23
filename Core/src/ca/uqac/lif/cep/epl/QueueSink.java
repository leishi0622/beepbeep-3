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
package ca.uqac.lif.cep.epl;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Sink that accumulates events into queues.
 * 
 * @author Sylvain Hallé
 *
 */
public class QueueSink extends Sink
{
	protected Queue<Object>[] m_queues;
	
	public QueueSink(int in_arity)
	{
		super(in_arity);
		reset();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void reset()
	{
		super.reset();
		int arity = getInputArity();
		m_queues = new Queue[arity];
		for (int i = 0; i < arity; i++)
		{
			m_queues[i] = new ArrayDeque<Object>();
		}

	}

	@Override
	protected Queue<Object[]> compute(Object[] inputs)
	{
		for (int i = 0; i < m_queues.length; i++)
		{
			Queue<Object> q = m_queues[i];
			if (inputs[i] != null)
			{
				q.add(inputs[i]);
			}
		}
		return wrapVector(new Object[m_queues.length]);
	}
	
	/**
	 * Gets the queue corresponding to the <i>i</i>-th output of the sink 
	 * @param i The position of the output. Must be non-negative and less than
	 *   the queue's arity.
	 * @return The queue
	 */
	/*@requires i >= 0 && i < m_queues.length */
	public Queue<Object> getQueue(int i)
	{
		return m_queues[i];
	}
	
	/**
	 * Removes the first event of all queues
	 * @return A vector containing the first event of all queues, or null
	 */
	public Object[] remove()
	{
		Object[] out = new Object[m_queues.length];
		for (int i = 0; i < m_queues.length; i++)
		{
			Queue<Object> q = m_queues[i];
			if (q.isEmpty())
			{
				out[i] = null;
			}
			else
			{
				Object o = q.remove();
				out[i] = o;				
			}
		}
		return out;
	}
	
	@Override
	public QueueSink clone()
	{
		return new QueueSink(getInputArity());
	}
}
