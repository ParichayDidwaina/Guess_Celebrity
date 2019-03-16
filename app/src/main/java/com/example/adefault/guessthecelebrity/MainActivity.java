package com.example.adefault.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView e;
    int opt,ans,otherans;
    String imagecodes="";
    String namecodes="";
    String namecodes1[];
    Pattern patt;
    Matcher m;
    String imagecodes1[];
    Random r = new Random();

    public void randomise()
    {
        android.support.v7.widget.GridLayout g = (android.support.v7.widget.GridLayout) findViewById(R.id.grid1);
        int x = r.nextInt(71);
        setimage(x);
        opt=r.nextInt(4);
        Button b = (Button)g.getChildAt(opt);
        b.setText(namecodes1[x]);
        ans=Integer.parseInt(b.getTag().toString());
        for(int i=0;i<4;i++)
        {
            if(i!=opt)
            {
                otherans = r.nextInt(71);
                if(otherans!=x)
                {
                    ((Button)g.getChildAt(i)).setText(namecodes1[otherans]);
                }
                else
                {
                    while(otherans==x)
                    {
                        otherans = r.nextInt(71);
                    }
                    ((Button)g.getChildAt(i)).setText(namecodes1[otherans]);
                }
            }
        }

    }
    public void setimage(int x)
    {
        images img = new images();
        Bitmap b;
        try {
            b = img.execute(imagecodes1[x]).get();
            e.setImageBitmap(b);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void click(View view)
    {
        Button button = (Button) view;
        if(Integer.parseInt(button.getTag().toString())==ans)
        {
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "WRONG :(", Toast.LENGTH_SHORT).show();
        }
        randomise();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = (ImageView) findViewById(R.id.imageView);
        download d = new download();
        String html;
        try{
            html = d.execute("http://www.posh24.se/kandisar").get();
            System.out.println(html);
            patt = Pattern.compile("src=\"http(.*?)\"");
            m = patt.matcher(html);

            while (m.find())
            {
                imagecodes+="http"+m.group(1)+",";
            }
            imagecodes1= imagecodes.split(",");

            patt = Pattern.compile("alt=\"(.*?)\"");
            m = patt.matcher(html);
            while (m.find())
            {
                namecodes+=m.group(1)+",";
            }
            namecodes1=namecodes.split(",");

            System.out.print("\n*****************************************************************************\n");
            for(String temp:imagecodes1)
            {
                System.out.println(temp);

            }

            System.out.print("\n*****************************************************************************\n");

            for(String temp:namecodes1)
            {
                System.out.println(temp);

            }

            System.out.print("\n*****************************************************************************\n");

            randomise();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public class download extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            try{
                URL url = new URL(urls[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.connect();
                InputStream i = c.getInputStream();
                InputStreamReader r = new InputStreamReader(i);
                int data = r.read();
                while(data!=-1)
                {
                    char ch = (char)data;
                    result+=ch;
                    data=r.read();
                }

                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "INTERNET DOWN";
            }

        }
    }

    public class images extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream i = connection.getInputStream();
                Bitmap b = BitmapFactory.decodeStream(i);
                return b;
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }
}
