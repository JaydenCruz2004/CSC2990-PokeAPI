package com.example.csc2990_pokeapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView numberTV, nameTV, heightTV, baseTV, weightTV, sigText;
    ImageView pokePic;
    Button searchButton, clearButton;
    EditText userInput;
    ListView pokeList;

    ArrayList<String> pokemons = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        numberTV = findViewById(R.id.number_tv);
        nameTV = findViewById(R.id.pokemon_name);
        heightTV = findViewById(R.id.height_tv);
        baseTV = findViewById(R.id.basexp_tv);
        weightTV = findViewById(R.id.weight_tv);
        pokePic = findViewById(R.id.imageView);
        searchButton = findViewById(R.id.search_button);
        clearButton = findViewById(R.id.clear_button);
        userInput = findViewById(R.id.user_input);
        pokeList = findViewById(R.id.poke_listview);
        sigText = findViewById(R.id.signature_text);

        searchButton.setOnClickListener(v -> searchPokemon());
        clearButton.setOnClickListener(v -> clearDatabase());

        pokeList.setOnItemClickListener((parent, view, position, id) -> {
            String item = pokemons.get(position);
            String[] parts = item.split(":");
            fetchPokemon(parts[1].trim().toLowerCase());
        });

        fetchPokemon("pikachu");
    }

    private void searchPokemon() {
        String input = userInput.getText().toString().trim().toLowerCase();
        userInput.setText("");

        if (input.isEmpty()) {
            Toast.makeText(this, "Enter a Pokémon name", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchPokemon(input);
    }

    private void fetchPokemon(String name) {
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            if (conn.getResponseCode() != 200) {
                Toast.makeText(this, "Pokémon not found", Toast.LENGTH_LONG).show();
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                json.append(line);

            JSONObject obj = new JSONObject(json.toString());

            String pokeName = obj.getString("name");
            int id = obj.getInt("id");
            int weight = obj.getInt("weight");
            int height = obj.getInt("height");
            int baseExp = obj.getInt("base_experience");
            String sprite = obj.getJSONObject("sprites").getString("front_default");
            String ability = obj.getJSONArray("abilities").getJSONObject(0)
                    .getJSONObject("ability").getString("name");
            String move = obj.getJSONArray("moves").getJSONObject(0)
                    .getJSONObject("move").getString("name");

            updateUI(pokeName, id, weight, height, baseExp, sprite, ability, move);
            saveToDB(pokeName, id, height, weight, baseExp);
            queryDB();

        } catch (Exception e) {
            Log.e("err", "error loading pokemon", e);
            Toast.makeText(this, "Could not load Pokémon", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String name, int id, int weight, int height, int baseExp,
                          String sprite, String ability, String move) {

        nameTV.setText(name.toUpperCase());
        numberTV.setText(String.valueOf(id));
        weightTV.setText(String.valueOf(weight));
        heightTV.setText(String.valueOf(height));
        baseTV.setText(String.valueOf(baseExp));
        sigText.setText("Signature: " + move.toUpperCase() + " / " + ability.toUpperCase());

        Picasso.get().load(sprite).into(pokePic);
    }

    private void saveToDB(String name, int id, int height, int weight, int xp) {

        Cursor c = getContentResolver().query(
                PokemonDBProvider.CONTENT_URI,
                null,
                "Number = ?",
                new String[]{String.valueOf(id)},
                null
        );

        if (c != null && c.getCount() > 0) {
            c.close();
            return; // do not insert duplicates
        }

        ContentValues values = new ContentValues();
        values.put(PokemonDBProvider.COLUMN_ONE, name);
        values.put(PokemonDBProvider.COLUMN_TWO, String.valueOf(id));
        values.put(PokemonDBProvider.COLUMN_THREE, String.valueOf(height));
        values.put(PokemonDBProvider.COLUMN_FOUR, String.valueOf(weight));
        values.put(PokemonDBProvider.COLUMN_FIVE, String.valueOf(xp));

        getContentResolver().insert(PokemonDBProvider.CONTENT_URI, values);
    }

    private void queryDB() {
        pokemons.clear();
        mCursor = getContentResolver().query(PokemonDBProvider.CONTENT_URI, null, null, null, null);

        if (mCursor == null) return;

        while (mCursor.moveToNext()) {
            String name = mCursor.getString(1);
            String number = mCursor.getString(2);
            pokemons.add(number + ": " + name);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pokemons);
        pokeList.setAdapter(adapter);
    }

    private void clearDatabase() {
        getContentResolver().delete(PokemonDBProvider.CONTENT_URI, null, null);
        pokemons.clear();
        if (adapter != null) adapter.notifyDataSetChanged();

        numberTV.setText("");
        heightTV.setText("");
        weightTV.setText("");
        baseTV.setText("");
        nameTV.setText("POKEMON");
        sigText.setText("Signature Move/Ability");
        pokePic.setImageURI(Uri.EMPTY);
    }
}
