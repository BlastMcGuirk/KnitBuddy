package com.apps.bguirks.knitbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.apps.bguirks.knitbuddy.adapters.CustomExpandableListAdapter;
import com.apps.bguirks.knitbuddy.database.DatabaseHelper;
import com.apps.bguirks.knitbuddy.database.dataobjects.Category;
import com.apps.bguirks.knitbuddy.database.dataobjects.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Context mainActivityContext;
    private DatabaseHelper db;

    private ExpandableListView listView;
    private HashMap<Category, List<Project>> listViewData;
    private List<Category> categories;
    private CustomExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainActivityContext = this;
        setContentView(R.layout.activity_main);

        // Setup Database
        db = new DatabaseHelper(this);
        if (db.categories.getTableCount() == 0) {
            long fallId = db.categories.add(new Category("Fall"));
            long winterId = db.categories.add(new Category("Winter"));

            db.projects.add(new Project("Brendo's Hat", winterId, "Straight", "10 US", "Yarn-Ease", "6"));
            db.projects.add(new Project("Mark's Hat", winterId));
            db.projects.add(new Project("Pumpkins", fallId));
            db.projects.add(new Project("Pillow", fallId));
        }

        // Get the data from the database
        listViewData = getListViewData();
        categories = new ArrayList<>(listViewData.keySet());
        Collections.sort(categories);

        // Set the content of the ExpandableListView component
        listView = findViewById(R.id.category_list_view);
        expandableListAdapter = new CustomExpandableListAdapter(
                this, categories, listViewData);
        listView.setAdapter(expandableListAdapter);

        // Set the listeners for the ExpandableListView
        setupListViewListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the data from the database
        listViewData = getListViewData();
        categories = new ArrayList<>(listViewData.keySet());
        Collections.sort(categories);

        // Set the content of the ExpandableListView component
        listView = findViewById(R.id.category_list_view);
        expandableListAdapter = new CustomExpandableListAdapter(
                this, categories, listViewData);
        listView.setAdapter(expandableListAdapter);

        // Set the listeners for the ExpandableListView
        setupListViewListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                new AlertDialog.Builder(mainActivityContext)
                        .setTitle("Add New Item")
                        .setItems(new String[]{"Category", "Project", "Cancel"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    // Category
                                    case 0:
                                        dialog.cancel();

                                        final EditText categoryInput = new EditText(mainActivityContext);
                                        categoryInput.setInputType(InputType.TYPE_CLASS_TEXT);
                                        new AlertDialog.Builder(mainActivityContext)
                                                .setTitle("Category Name")
                                                .setView(categoryInput)
                                                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String categoryName = categoryInput.getText().toString();
                                                        db.categories.add(new Category(categoryName));
                                                        refreshData();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();
                                        break;
                                    // Project
                                    case 1:
                                        dialog.cancel();

                                        final EditText projectInput = new EditText(mainActivityContext);
                                        projectInput.setInputType(InputType.TYPE_CLASS_TEXT);
                                        new AlertDialog.Builder(mainActivityContext)
                                                .setTitle("Project Name")
                                                .setView(projectInput)
                                                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String projectName = projectInput.getText().toString();
                                                        long defaultCategoryId = db.categories.getAll().get(0).get_id();
                                                        Project newProject = new Project(projectName, defaultCategoryId);
                                                        long projectId = db.projects.add(newProject);

                                                        // Open the project
                                                        Intent switchToProject = new Intent(mainActivityContext, ProjectActivity.class);
                                                        switchToProject.putExtra("PROJECT_ID", projectId);

                                                        startActivity(switchToProject);
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();
                                        break;
                                    // Cancel
                                    default:
                                        dialog.cancel();
                                }
                            }
                        })
                        .show();
                break;
        }
        return true;
    }

    private HashMap<Category, List<Project>> getListViewData() {
        HashMap<Category, List<Project>> data = new HashMap<>();
        List<Category> categories = db.categories.getAll();

        for (Category category : categories) {
            data.put(category, db.projects.getAll(category.get_id()));
        }

        return data;
    }

    private void setupListViewListeners() {
        this.listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent switchToProject = new Intent(mainActivityContext, ProjectActivity.class);

                Project project = listViewData.get(categories.get(groupPosition)).get(childPosition);
                switchToProject.putExtra("PROJECT_ID", project.get_id());

                startActivity(switchToProject);
                return true;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    final Project project = listViewData.get(categories.get(groupPosition))
                            .get(childPosition);

                    new AlertDialog.Builder(mainActivityContext)
                            .setTitle("Delete Project: " + project.getProjectName())
                            .setMessage("Are you sure you want to delete this project?")

                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.projects.delete(project.get_id());
                                    refreshData();
                                }
                            })

                            .setNegativeButton("Cancel", null)
                            .setCancelable(true)
                            .show();

                } else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    final Category category = categories.get(groupPosition);

                    new AlertDialog.Builder(mainActivityContext)
                            .setTitle("Delete Category: " + category.getCategory())
                            .setMessage("Are you sure you want to delete this category and all projects under it?")

                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.categories.delete(category.get_id());
                                    refreshData();
                                }
                            })

                            .setNegativeButton("Cancel", null)
                            .setCancelable(true)
                            .show();
                }
                return true;
            }
        });
    }

    private void refreshData() {
        listViewData = getListViewData();
        categories = new ArrayList<>(listViewData.keySet());
        Collections.sort(categories);

        expandableListAdapter.updateData(categories, listViewData);
        expandableListAdapter.notifyDataSetChanged();
    }
}