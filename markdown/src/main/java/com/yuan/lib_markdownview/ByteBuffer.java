package com.yuan.lib_markdownview;

import android.content.Context;
import android.net.Uri;

import java.io.*;
import java.util.Arrays;

/**
 *
 */
public class ByteBuffer {
    byte[] buf;
    int count;

    public ByteBuffer(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        buf = new byte[size];
    }

    public ByteBuffer(byte[] buf) {
        this.buf = buf;
        this.count = buf.length;
    }

    public byte[] getData() {
        return this.buf;
    }

    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buf.length > 0)
            grow(minCapacity);
    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        buf = Arrays.copyOf(buf, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public synchronized void write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(count + len);
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    public synchronized int size() {
        return count;
    }

    public static final ByteBuffer create(InputStream is, int size) throws IOException {

        ByteBuffer data = null;

        if (size <= 0) {
            size = 600 * 1024;

            ByteBuffer stream = new ByteBuffer(size);

            byte[] buf = new byte[200 * 1024];
            int length;
            while ((length = is.read(buf)) >= 0) {
                stream.write(buf, 0, length);
            }

        } else {

            byte[] buf = new byte[size];
            int offset = 0;
            while (true) {
                int num = is.read(buf, offset, (size - offset));
                if (num < 0) {
                    break;
                }

                offset += num;
                if (offset == size) {
                    break;
                }
            }

            data = new ByteBuffer(buf);
        }

        return data;
    }

    public static final ByteBuffer create(Context context, Uri data, long size) {
        ByteBuffer buf = null;

        {
            InputStream is = null;

            try {
                is = context.getContentResolver().openInputStream(data);
                buf = ByteBuffer.create(is, (int) size);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return buf;
    }

    public static final ByteBuffer create(File file) {
        if (!file.exists()) {
            return null;
        }

        ByteBuffer buf = null;

        {
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(file);

                buf = ByteBuffer.create(fis, (int) (file.length()));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return buf;
    }
}
