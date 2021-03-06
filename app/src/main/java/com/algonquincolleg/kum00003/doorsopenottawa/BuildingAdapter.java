package com.algonquincolleg.kum00003.doorsopenottawa;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import model.Building;

/**
 * Purpose: customize the Planet cell for each planet displayed in the ListActivity (i.e. MainActivity).
 * Usage:
 *   1) extend from class ArrayAdapter<YourModelClass>
 *   2) @override getView( ) :: decorate the list cell
 *
 * Based on the Adapter OO Design Pattern.
 *
 * /**
 * Created by Sercan on 11/22/16.
 * Created by David on 11/22/16.
 *
 *
 * Reference: based on DisplayList in "Connecting Android Apps to RESTful Web Services" with David Gassner
 */

public class BuildingAdapter extends ArrayAdapter<Building> {

    private Context context;
    private List<Building> buildingList;

    // TODO: cache the binary image for each planet
    private LruCache<Integer, Bitmap> imageCache;

    public BuildingAdapter(Context context, int resource, List<Building> objects) {
        super(context, resource, objects);
        this.context = context;
        this.buildingList = objects;

        // TODO: instantiate the imageCache
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_buldings, parent, false);

        //Display planet name in the TextView widget
        Building building = buildingList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(building.getName());
        TextView tv2 = (TextView) view.findViewById(R.id.textView2);
        tv2.setText(building.getAddress());

        Bitmap bitmap = imageCache.get(building.getBuildingID());
        if (bitmap != null) {
            Log.i( "BUILDINGS", building.getName() + "\tbitmap in cache");
            ImageView image = (ImageView) view.findViewById(R.id.imageView1);
            image.setImageBitmap(building.getBitmap());
        }
        else {
            Log.i( "BUILDINGS", building.getName() + "\tfetching bitmap using AsyncTask");
            BuildingAndView container = new BuildingAndView();
            container.building = building;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }

        return view;

    }
    private class BuildingAndView{
        public Building building;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<BuildingAndView, Void, BuildingAndView> {

        @Override
        protected BuildingAndView doInBackground(BuildingAndView... params) {

            BuildingAndView container = params[0];
            Building building = container.building;

            try {
                String imageUrl = MainActivity.IMAGES_BASE_URL + building.getImage();
                InputStream in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                building.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                System.err.println("IMAGE: " + building.getName() );
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(BuildingAndView result) {
            ImageView image = (ImageView) result.view.findViewById(R.id.imageView1);
            image.setImageBitmap(result.bitmap);
//            result.planet.setBitmap(result.bitmap);
            imageCache.put(result.building.getBuildingID(), result.bitmap);
        }
    }



}
