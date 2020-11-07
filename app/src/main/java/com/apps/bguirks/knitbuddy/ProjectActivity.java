package com.apps.bguirks.knitbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.apps.bguirks.knitbuddy.database.DatabaseHelper;
import com.apps.bguirks.knitbuddy.database.dataobjects.Category;
import com.apps.bguirks.knitbuddy.database.dataobjects.Project;

import java.util.List;

public class ProjectActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private boolean editing;

    private DatabaseHelper db;
    private Project project;

    private List<Category> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        this.db = new DatabaseHelper(this);
        this.editing = true;

        Intent intent = getIntent();
        long projectId = intent.getLongExtra("PROJECT_ID", -1);
        if (projectId != -1) {
            this.project = db.projects.get(projectId);
            editing = false;
        }

        categoriesList = db.categories.getAll();

        String title = this.project == null ? "New Project" : this.project.getProjectName();
        setTitle(title);

        setViewsContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.project_activity_menu, menu);

        findViewById(R.id.title_edit).setOnFocusChangeListener(this);
        findViewById(R.id.needle_type_edit).setOnFocusChangeListener(this);
        findViewById(R.id.needle_size_edit).setOnFocusChangeListener(this);
        findViewById(R.id.yarn_brand_edit).setOnFocusChangeListener(this);
        findViewById(R.id.yarn_size_edit).setOnFocusChangeListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_project:
                this.editing = !this.editing;
                item.setTitle(this.editing ? "Save" : "Edit");
                item.setShowAsAction(this.editing ? MenuItem.SHOW_AS_ACTION_ALWAYS : MenuItem.SHOW_AS_ACTION_NEVER);
                if (!this.editing) {
                    updateAndSaveInfo();
                }
                toggleViews();
        }
        return true;
    }

    private void updateAndSaveInfo() {
        this.project.setProjectName(((EditText)findViewById(R.id.title_edit)).getText().toString());
        this.project.setCategoryId(((Category)(((Spinner)findViewById(R.id.category_edit)).getSelectedItem())).get_id());
        this.project.setNeedleType(((EditText)findViewById(R.id.needle_type_edit)).getText().toString());
        this.project.setNeedleSize(((EditText)findViewById(R.id.needle_size_edit)).getText().toString());
        this.project.setYarnBrand(((EditText)findViewById(R.id.yarn_brand_edit)).getText().toString());
        this.project.setYarnSize(((EditText)findViewById(R.id.yarn_size_edit)).getText().toString());

        this.db.projects.update(this.project);

        this.setTitle((this.project.getProjectName()));

        this.setViewsContent();
    }

    private void toggleViews() {
        int visibility = this.editing ? View.GONE : View.VISIBLE;

        findViewById(R.id.title_value).setVisibility(visibility);
        findViewById(R.id.category_value).setVisibility(visibility);
        findViewById(R.id.needle_type_value).setVisibility(visibility);
        findViewById(R.id.needle_size_value).setVisibility(visibility);
        findViewById(R.id.yarn_brand_value).setVisibility(visibility);
        findViewById(R.id.yarn_size_value).setVisibility(visibility);

        visibility = this.editing ? View.VISIBLE : View.GONE;

        findViewById(R.id.title_edit).setVisibility(visibility);
        findViewById(R.id.category_edit).setVisibility(visibility);
        findViewById(R.id.needle_type_edit).setVisibility(visibility);
        findViewById(R.id.needle_size_edit).setVisibility(visibility);
        findViewById(R.id.yarn_brand_edit).setVisibility(visibility);
        findViewById(R.id.yarn_size_edit).setVisibility(visibility);
    }

    private void setViewsContent() {
        // Category ID
        String title = "";
        Category category = categoriesList.get(0);
        String needleType = "";
        String needleSize = "";
        String yarnBrand = "";
        String yarnSize = "";

        if (project != null) {
            title = project.getProjectName();
            category = db.categories.get(project.getCategoryId());
            System.out.println(category);
            needleType = project.getNeedleType();
            needleSize = project.getNeedleSize();
            yarnBrand = project.getYarnBrand();
            yarnSize = project.getYarnSize();
        }

        // Title
        ((TextView)findViewById(R.id.title_value)).setText(title);
        ((EditText)findViewById(R.id.title_edit)).setText(title);

        // Category
        ((TextView)findViewById(R.id.category_value)).setText(category.toString());
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(this.categoriesList);
        Spinner spinner = findViewById(R.id.category_edit);
        spinner.setAdapter(adapter);
        int selection = this.categoriesList.indexOf(category);
        spinner.setSelection(selection);

        // Needle Type
        ((TextView)findViewById(R.id.needle_type_value)).setText(needleType);
        ((EditText)findViewById(R.id.needle_type_edit)).setText(needleType);

        // Needle Size
        ((TextView)findViewById(R.id.needle_size_value)).setText(needleSize);
        ((EditText)findViewById(R.id.needle_size_edit)).setText(needleSize);

        // Yarn Brand
        ((TextView)findViewById(R.id.yarn_brand_value)).setText(yarnBrand);
        ((EditText)findViewById(R.id.yarn_brand_edit)).setText(yarnBrand);

        // Yarn Size
        ((TextView)findViewById(R.id.yarn_size_value)).setText(yarnSize);
        ((EditText)findViewById(R.id.yarn_size_edit)).setText(yarnSize);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}