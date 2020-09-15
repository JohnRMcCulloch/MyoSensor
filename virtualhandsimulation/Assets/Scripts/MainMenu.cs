using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MainMenu : MonoBehaviour
{
    //Logging comment used to write to console if QUIT selected
    //Used for reference in Unity as program will not close but can see action is logged
    private string loggingComment = "QUIT!";

    //Change scene
    public void PlayGame()
    {
        //Load next scene (next scene in the que)
        //Get current active scene +1 (next in scene que)
        try
        {
            SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex + 1);
        }
        catch (Exception)
        {
        }
    }

    public void LoadMainMenu()
    {
        //Load previous scene (MainMenu)
        try
        {
            SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex - 1);
        }
        catch (Exception)
        {
        }
    }

    public void QuitGame()
    {
        //Log message in console to show working inside of unity
        Debug.Log(loggingComment);
        //Quit Application
        Application.Quit();
    }
}
