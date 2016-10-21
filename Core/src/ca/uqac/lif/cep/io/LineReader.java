package ca.uqac.lif.cep.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Queue;
import java.util.Scanner;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SingleProcessor;

/**
 * Special case of stream reader reading lines from an input stream.
 * @author Sylvain Hallé
 *
 */
public class LineReader extends SingleProcessor 
{
	/**
	 * The scanner to read from
	 */
	protected Scanner m_scanner;

	/**
	 * The input stream to read from
	 */
	protected InputStream m_inStream;

	/**
	 * Whether to update a status line about the number of lines read
	 */
	public static boolean s_printStatus = false;

	/**
	 * Whether to add a carriage return at the end of each line
	 */
	protected boolean m_addCrlf = true;

	/**
	 * Creates a new LineReader from a File
	 * @param f The file to read from
	 * @throws FileNotFoundException If file is not found
	 */
	public LineReader(File f) throws FileNotFoundException
	{
		this(new FileInputStream(f));
	}
	
	/**
	 * Creates a new file reader from an input stream
	 * @param is The input stream to read from
	 */
	public LineReader(InputStream is)
	{
		super(0, 1);
		m_inStream = is;
		m_scanner = new Scanner(is);
	}
	
	/**
	 * Tells the reader to add a carriage return at the end of each
	 * output event
	 * @param b true to add a CRLF, false otherwise
	 * @return This reader
	 */
	public LineReader addCrlf(boolean b)
	{
		m_addCrlf = b;
		return this;
	}

	/*@Override
	public Pullable getPullableOutput(int index)
	{
		return new SentinelPullable();
	}*/

	@Override
	protected Queue<Object[]> compute(Object[] inputs) 
	{
		if (m_scanner.hasNextLine())
		{
			String line = m_scanner.nextLine();
			if (m_addCrlf)
			{
				line += "\n";
			}
			return wrapObject(line);
		}
		return null;
	}

	@Override
	public Processor clone() 
	{
		return new LineReader(m_inStream);
	}
}
