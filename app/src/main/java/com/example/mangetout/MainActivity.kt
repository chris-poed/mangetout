package com.example.mangetout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mangetout.ui.theme.MangetoutTheme

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String>,
    val method: String,
    val time: String,
    val allergy: String,
    val cuisine: String,
    val imageResId: Int
)

val recipes = listOf(
    Recipe(
        id = 1,
        title = "How to Boil an Egg",
        ingredients = listOf(
            "2 eggs",
            "Water",
            "Pinch of salt"
        ),
        method = "Place the eggs in a saucepan and cover with cold water. Bring to the boil, then simmer for 6-8 minutes depending on how firm you want the yolk. Drain and cool under cold running water.",
        time = "10 minutes",
        allergy = "Eggs",
        cuisine = "British",
        imageResId = R.drawable.recipe_placeholder
    ),
    Recipe(
        id = 2,
        title = "Village Lentils",
        ingredients = listOf(
            "1 cup red lentils",
            "1 onion, chopped",
            "2 garlic cloves, crushed",
            "1 tsp cumin",
            "500ml vegetable stock",
            "Salt and pepper"
        ),
        method = "Fry the onion until soft, then add the garlic and cumin. Stir in the lentils and vegetable stock. Simmer for 20-25 minutes until the lentils are soft. Season before serving.",
        time = "30 minutes",
        allergy = "None",
        cuisine = "Indian-inspired",
        imageResId = R.drawable.recipe_placeholder
    ),
    Recipe(
        id = 3,
        title = "Tomato Pasta",
        ingredients = listOf(
            "200g pasta",
            "1 tin chopped tomatoes",
            "1 garlic clove",
            "1 tbsp olive oil",
            "Fresh basil",
            "Salt and pepper"
        ),
        method = "Cook the pasta according to the packet instructions. Meanwhile, fry the garlic in olive oil, add the tomatoes, and simmer for 10 minutes. Stir through the cooked pasta and finish with basil.",
        time = "20 minutes",
        allergy = "Gluten",
        cuisine = "Italian",
        imageResId = R.drawable.recipe_placeholder
    ),
    Recipe(
        id = 4,
        title = "Simple Veggie Stir Fry",
        ingredients = listOf(
            "Mixed vegetables",
            "Soy sauce",
            "Noodles",
            "1 garlic clove",
            "1 tsp sesame oil"
        ),
        method = "Cook the noodles, then set aside. Stir fry the vegetables with garlic and sesame oil for 5-7 minutes. Add the noodles and soy sauce, then toss everything together.",
        time = "15 minutes",
        allergy = "Gluten, sesame, soy",
        cuisine = "Asian-inspired",
        imageResId = R.drawable.recipe_placeholder
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MangetoutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "recipeList"
    ) {
        composable("recipeList") {
            RecipeListScreen(
                recipes = recipes,
                onRecipeClick = { recipe ->
                    navController.navigate("recipeDetail/${recipe.id}")
                }
            )
        }

        composable("recipeDetail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments
                ?.getString("recipeId")
                ?.toIntOrNull()

            val recipe = recipes.find { it.id == recipeId }

            if (recipe != null) {
                RecipeDetailsScreen(
                    recipe = recipe,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mangetout Recipes")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = {
                        onRecipeClick(recipe)
                    }
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = recipe.imageResId),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${recipe.time} • ${recipe.cuisine}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Allergy: ${recipe.allergy}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipe: Recipe,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(recipe.title)
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = recipe.imageResId),
                            contentDescription = recipe.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = recipe.title,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Time: ${recipe.time}")
                            Text("Cuisine: ${recipe.cuisine}")
                            Text("Allergy: ${recipe.allergy}")
                        }
                    }
                }
            }

            item {
                DetailSection(title = "Ingredients") {
                    recipe.ingredients.forEach { ingredient ->
                        Text(
                            text = "• $ingredient",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            item {
                DetailSection(title = "Method") {
                    Text(
                        text = recipe.method,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}