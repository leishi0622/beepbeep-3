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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.functions.Function;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.SingleProcessor;

/**
 * Separates an input trace into different "slices". The slicer
 * takes as input a processor <i>p</i> and
 * a slicing function <i>f</i>.
 * There exists potentially one instance of <i>p</i> for each value
 * in the image of <i>f</i>.
 * <p>
 * When an event <i>e</i> arrives, the slicer evaluates
 * <i>c</i> = <i>f</i>(<i>e</i>). This value determines to what instance
 * of <i>p</i> the event will be dispatched. If no slice with such
 * value currently exists, a new instance of <i>p</i> will
 * be created. The processor then outputs
 * a <code>Collection</code> object (typically a set) containing the
 * <em>last</em> events returned by every slice.
 * <p>
 * The function <i>f</i> may return <code>null</code>, or the special
 * object {@link ToAllSlices}. This indicates that no new slice must
 * be created, but that the incoming event must be dispatched to
 * <em>all</em> slices one by one.
 *  
 * @author Sylvain Hallé
 */
public class StateSlicer extends SingleProcessor
{
	/**
	 * The slicing function
	 */
	protected Function m_slicingFunction = null;

	/**
	 * The internal processor
	 */
	protected Processor m_processor = null;

	/**
	 * The cleaning function
	 */
	protected Function m_cleaningFunction = null;

	protected Map<Object,Processor> m_slices;

	protected Map<Object,QueueSink> m_sinks; 

	/**
	 * The last value output by the processor for each slice
	 */
	protected List<Object> m_lastValues;

	/**
	 * A map between slices and their index in the various arrays 
	 */
	protected Map<Object,Integer> m_sliceIndices;

	/**
	 * The values of the slices that are no longer changing
	 */
	protected List<Object> m_fixedValues;

	StateSlicer()
	{
		super(1, 1);
	}

	public StateSlicer(/*@NonNull*/ Function func, /*@NonNull*/ Processor proc, /*@Null*/ Function clean_func)
	{
		super(proc.getInputArity(), proc.getOutputArity());
		m_processor = proc;
		m_slicingFunction = func;
		m_cleaningFunction = clean_func;
		m_slices = new HashMap<Object,Processor>();
		m_sinks = new HashMap<Object,QueueSink>();
		m_sliceIndices = new HashMap<Object,Integer>();
		m_lastValues = new ArrayList<Object>();
		m_fixedValues = new LinkedList<Object>();
	}

	public StateSlicer(/*@NonNull*/ Function func, /*@NonNull*/ Processor proc)
	{
		this(func, proc, null);
	}

	@Override
	protected Queue<Object[]> compute(Object[] inputs) 
	{
		int output_arity = getOutputArity();
		Object[] f_value = m_slicingFunction.evaluate(inputs);
		Object slice_id = f_value[0];
		Set<Object> slices_to_process = new HashSet<Object>();
		if (slice_id instanceof ToAllSlices || slice_id == null)
		{
			slices_to_process.addAll(m_slices.keySet());
		}
		else
		{
			if (!m_slices.containsKey(slice_id))
			{
				// First time we see this value: create new slice
				Processor p = m_processor.clone();
				m_slices.put(slice_id, p);
				addContextFromSlice(p, slice_id);
				QueueSink sink = new QueueSink(output_arity);
				try 
				{
					Connector.connect(p, sink);
				} 
				catch (ConnectorException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_sinks.put(slice_id, sink);
				// Put dummy value temporarily
				m_lastValues.add(null);
				m_sliceIndices.put(slice_id, m_lastValues.size() - 1);
			}
			slices_to_process.add(slice_id);
		}
		for (Object s_id : slices_to_process)
		{
			// Find processor corresponding to that slice
			Processor slice_p = m_slices.get(s_id);
			// If this slice hasn't been cleaned up...
			if (slice_p != null)
			{
				QueueSink sink_p = m_sinks.get(s_id);
				// Push the input into the processor
				for (int i = 0; i < inputs.length; i++)
				{
					Object o_i = inputs[i];
					Pushable p = slice_p.getPushableInput(i);
					p.push(o_i);
				}
				// Collect the output from that processor
				Object[] out = sink_p.remove();
				// Can we clean that slice?
				Object[] can_clean = null;
				if (m_cleaningFunction != null)
				{
					can_clean = m_cleaningFunction.evaluate(out);
				}
				if (can_clean != null && can_clean.length > 0 && can_clean[0] instanceof Boolean && (Boolean) (can_clean[0]) == true)
				{
					// Yes: set the processor for that slice to null so it
					// can be garbage collected
					m_slices.put(s_id, null);
					m_sinks.put(s_id, null);
				}
				m_lastValues.set(m_sliceIndices.get(s_id), out[0]);
			}
		}
		return wrapObject(m_lastValues);
	}

	/**
	 * Gets a new empty instance of the collection used to contain the
	 * last returned value in each slice. By default, this collection
	 * is a set, but descendants of this class may override this method
	 * to provide a more appropriate type of collection.
	 * @return The collection
	 */
	public Collection<Object> newCollection()
	{
		return new ArrayList<Object>(m_lastValues.size());
	}

	/**
	 * Adds elements to the context of a newly created slice.
	 * By default, nothing is done. Descendants of this class may
	 * want to add context elements to the processor, based on the
	 * value of the newly created slice.
	 * @param p The newly created processor
	 * @param slice The value associated to the slice
	 */
	public void addContextFromSlice(Processor p, Object slice)
	{
		// By default, do nothing
	}

	@Override
	public void reset()
	{
		super.reset();
		m_slices.clear();
		m_slicingFunction.reset();
		m_cleaningFunction.reset();
	}

	/**
	 * Gets the number of slices the slicer currently handles
	 * @return The number of slices
	 */
	public int getActiveSliceCount()
	{
		return m_slices.size();
	}

	/**
	 * Gets the number of slices that have been cleaned up by the slicer
	 * @return The number of slices
	 */
	public int getClosedSliceCount()
	{
		return m_fixedValues.size();
	}

	public static void build(Stack<Object> stack) throws ConnectorException
	{
		Function f = (Function) stack.pop();
		stack.pop(); // ON
		stack.pop(); // (
		Processor p2 = (Processor) stack.pop();
		stack.pop(); // )
		stack.pop(); // WITH
		stack.pop(); // (
		Processor p1 = (Processor) stack.pop();
		stack.pop(); // )
		stack.pop(); // SLICE
		StateSlicer out = new StateSlicer(f, p2);
		Connector.connect(p1, out);
		stack.push(out);
	}

	@Override
	public StateSlicer clone()
	{
		return new StateSlicer(m_slicingFunction.clone(m_context), m_processor.clone(), m_cleaningFunction.clone());
	}

	/**
	 * Dummy object telling the slicer that an event must be sent to
	 * all slices
	 */
	public static class ToAllSlices
	{
		public static final transient ToAllSlices instance = new ToAllSlices();

		private ToAllSlices()
		{
			super();
		}
	}
}
