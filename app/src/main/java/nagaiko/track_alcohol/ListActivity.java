package nagaiko.track_alcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;

import nagaiko.track_alcohol.models.Cocktail;

public class ListActivity extends AppCompatActivity implements
        ClickRecyclerAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private TextView textView;

    private static String[] names;
    private DataStorage dataStorage = DataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mRecyclerView = new RecyclerView(this);

        Cocktail[] data = (Cocktail[])dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST);

        String[] names = new String[data.length];
        for (int i = 0; i < data.length; i++){
            names[i] = data[i].name;
        }

        mRecyclerView.setAdapter(new ClickRecyclerAdapter(getLayoutInflater(), names, this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setHasFixedSize(true);
        setContentView(mRecyclerView);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, names[position], Toast.LENGTH_SHORT).show();
    }
}
