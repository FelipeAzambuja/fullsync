/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301, USA.
 *
 * For information about the authors of this project Have a look
 * at the AUTHORS file in the root of this project.
 */
package net.sourceforge.fullsync.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

public class BlockBuffer implements ExecutionBuffer {
	private final Logger logger;

	private int maxSize;
	private int maxEntries;
	private int freeBytes;
	private int numberBytes;
	private int numberEntries;
	private byte[] buffer;
	private Entry[] entries;
	private List<EntryFinishedListener> finishedListeners;

	public BlockBuffer(Logger logger) {
		this.logger = logger;

		maxSize = 1024 * 1024 * 10;
		maxEntries = 5000;
		numberBytes = 0;
		numberEntries = 0;
		freeBytes = maxSize;

		buffer = null;
		entries = null;

		finishedListeners = new ArrayList<>();
	}

	@Override
	public void load() {
		if (null == buffer) {
			buffer = new byte[maxSize];
			entries = new Entry[maxEntries];
		}
	}

	@Override
	public void unload() {
		buffer = null;
		entries = null;
	}

	@Override
	public void flush() throws IOException {
		for (int i = 0; i < numberEntries; ++i) {
			Entry e = entries[i];
			EntryDescriptor desc = e.getDescriptor();
			IOException ioe = null;
			try {
				OutputStream out = desc.getOutputStream();
				if (null != out) {
					out.write(buffer, e.getStart(), e.getLength());
				}
				if (e.isLastSegment()) {
					desc.finishWrite();
					String opDesc = desc.getOperationDescription();
					if (null != opDesc) {
						logger.info(opDesc);
					}
				}
			}
			catch (IOException ex) {
				ioe = ex;
				logger.error("Exception", ex);
			}
			if (e.isLastSegment()) {
				for (EntryFinishedListener listener : finishedListeners) {
					listener.entryFinished(desc, ioe);
				}
			}
		}
		Arrays.fill(entries, null);
		numberBytes = 0;
		numberEntries = 0;
		freeBytes = maxSize;
	}

	// may not read as much as length says
	protected Entry storeBytes(InputStream inStream, long length) throws IOException {
		if (length > freeBytes) {
			length = freeBytes;
		}

		int start = numberBytes;
		int read = inStream.read(buffer, start, (int) length);
		//FIXME: read might return -1 which subsequently (in flush) throws an exception
		numberBytes += read;
		freeBytes -= read;

		Entry entry = new Entry(start, read);
		entries[numberEntries] = entry;
		numberEntries++;

		return entry;
	}

	private int store(InputStream inStream, long alreadyRead, long lengthLeft, EntryDescriptor descriptor) throws IOException {
		Entry entry = storeBytes(inStream, lengthLeft);

		int s = Segment.MIDDLE;
		if (alreadyRead == 0) {
			s |= Segment.FIRST;
		}
		if (entry.getLength() == lengthLeft) {
			s |= Segment.LAST;
		}

		entry.setInternalSegment(s);
		entry.setDescriptor(descriptor);

		return entry.getLength();
	}

	private void storeEntry(InputStream data, long size, EntryDescriptor descriptor) throws IOException {
		long alreadyRead = 0;
		long lengthLeft = size;

		do {
			if ((lengthLeft > freeBytes) || (numberEntries == maxEntries)) {
				flush();
			}
			int read = store(data, alreadyRead, lengthLeft, descriptor);
			alreadyRead += read;
			lengthLeft -= read;
		} while (lengthLeft > 0);
	}

	@Override
	public void storeEntry(final EntryDescriptor descriptor) throws IOException {
		Entry entry;
		if (descriptor.getSize() == 0) {
			if (numberEntries == maxEntries) {
				flush();
			}
			entry = new Entry(numberBytes, 0);
			entry.setDescriptor(descriptor);
			entries[numberEntries] = entry;
			numberEntries++;
		}
		else {
			storeEntry(descriptor.getInputStream(), descriptor.getSize(), descriptor);
		}
		descriptor.finishStore();
	}

	@Override
	public void addEntryFinishedListener(final EntryFinishedListener listener) {
		finishedListeners.add(listener);
	}

	@Override
	public void removeEntryFinishedListener(final EntryFinishedListener listener) {
		finishedListeners.remove(listener);
	}
}
