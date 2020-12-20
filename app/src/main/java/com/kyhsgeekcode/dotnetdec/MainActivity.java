package com.kyhsgeekcode.dotnetdec;

import android.app.*;
import android.os.*;
import android.util.*;
import at.pollaknet.api.facile.*;
import at.pollaknet.api.facile.exception.*;
import at.pollaknet.api.facile.renderer.*;
import at.pollaknet.api.facile.symtab.symbols.scopes.*;
import java.io.*;
import at.pollaknet.api.facile.symtab.symbols.*;
import at.pollaknet.api.facile.code.*;
import at.pollaknet.api.facile.code.instruction.*;

public class MainActivity extends Activity 
{
	private static final String TAG="Dotnet";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		try {
			//create a reflector object for later access (ILAsmRenderer)
			FacileReflector facileReflector = Facile.load("/sdcard/hello.dll"/*, "../somewhere/myOptionalProgramDebugDatabase.pdb"*/);
			//load the assembly
			Assembly assembly = facileReflector.loadAssembly(); 
			if(assembly!=null) {
				//output some useful information
				for(Type type:assembly.getAllTypes())
				{
					for(at.pollaknet.api.facile.symtab.symbols.Property prop: type.getProperties())
					{
						for(Method method:prop.getMethods())
						{
							MethodBody mb=method.getMethodBody();
							for(CilInstruction cii: mb.getCilInstructions())
							{
								//cii.get
							}
						}
					}
				}
				Log.v(TAG,assembly.toExtendedString());
				//generate output
				ILAsmRenderer renderer = new ILAsmRenderer(facileReflector);
				File file=new File("/sdcard/dotnet/");
				file.mkdirs();
				renderer.renderSourceFilesToDirectory( assembly,file.getAbsolutePath() /*System.getProperty("user.dir")*/);
				//print out the location of the files 
				Log.v(TAG,"Generated decompiled files in: " + System.getProperty("user.dir"));
			} else {
				Log.v(TAG,"File maybe contains only resources...");			}
		} catch (DotNetContentNotFoundException e) {
			//maybe you selected a native executeable... 
			Log.v(TAG,"No .Net content found...");
			e.printStackTrace();
		} catch (IOException e) {
			//in case of missing file acces rights or similar problems
			Log.e(TAG,"",e);
			//e.printStackTrace();
		} catch (Exception e) {
			//everything else
			Log.e(TAG,"",e);
			//e.printStackTrace();
		}
    }
}
