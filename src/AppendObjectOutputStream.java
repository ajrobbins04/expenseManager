package src;
import java.io.ObjectOutputStream;    // write in objects
import java.io.OutputStream;
import java.io.IOException;   
/*
    * Is an extending subclass of the standard ObjectOutputStream
    * class. Will be used to ensure that Expense objects can be
    * appended to the existing ObjectOutputStream.
*/

public class AppendObjectOutputStream extends ObjectOutputStream {

    public AppendObjectOutputStream( OutputStream objectOut ) throws IOException
    {
        // initializes the AppendObjectOutputStream to the parent OutputStream
        super(objectOut);
    }

    @Override // will override OutputStream's writeStreamHeader() method
    protected void writeStreamHeader() throws IOException {

        // prevents file corruption by clearing the 
        // previous header and re-writing a new one
        reset();
    }
}