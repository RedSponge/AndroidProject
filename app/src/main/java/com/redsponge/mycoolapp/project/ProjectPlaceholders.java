package com.redsponge.mycoolapp.project;

import java.util.Random;

/**
 * A random class which holds placeholder strings for new projects
 */
public class ProjectPlaceholders {

    public static final String[] RANDOM_NAMES = {
            "New Project_0",
            "My Greatest Idea",
            "Marshmallow",
            "Le Masterpiece",
            "Art",
            "New Project!",
            "I'm running out of ideas for these",
            "MyProject.exe",
            "Visual Studio has stopped working",
            "Arrays start at 0",

            "Roses are red",
            "Violets are blue",
            "NullPointerException",
            "On Line 32",

            "supercalifragilisticexpialidocious",
            "A Normal Name",
            "A Serious Project"
    };

    public static final String[] RANDOM_DESCRIPTIONS = {
          "This will change the world",
          "You know it baby",

            "Let me tell you a story...",
            "You get an invite! and YOU get an invite!",
            "I need to come up with more",
            "This was supposed to be a serious project",
            "Now in 3D!",
            "Now in 4D!",
            "Now in 2,147,483,647D!",
            "Template Description",
            "Funny Joke",
            "Why did the chicken cross the road?",
            "The ultimate betrayal",
            "| || || |_",
            "^  This is the name ^",
            "Somebody once told me",
            "The world is gonna roll me"
    };

    private static final Random rnd = new Random();

    public static String getRandomName() {
        return RANDOM_NAMES[rnd.nextInt(RANDOM_NAMES.length)];
    }

    public static String getRandomDescription() {
        return RANDOM_DESCRIPTIONS[rnd.nextInt(RANDOM_DESCRIPTIONS.length)];
    }

}
