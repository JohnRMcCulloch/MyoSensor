using System;
using UnityEngine;

public class handAnimController : MonoBehaviour
{
    //Animator Hand Animation used as reference in unity
    public Animator handAnim;

    //Boolean pause used to start (Enable) and stop (Disable) animation play
    //private bool pause;

    //Boolean to track which state is active
    private bool state;

    //State One assigned to HandOpen
    private string stateOne = "HandOpen";

    //State Two assigned to HandPoint
    private string stateTwo = "HandPoint";

    //Control Direction assigned to Direction variable in Unity 
    private string controlDirection = "Direction";

    // Start is called before the first frame update
    void Start()
    {
        handAnim = GetComponent<Animator>();
        //initalise bool's
        //pause = false;
        state = true;
    }
    //Pause() used to control movement on key press (will no progress passed frame if key lifted)
    private void Pause(bool pause)
    {


        //change current state of pause bool to opposite
        //changed to opposite as new call (switch between states)
        //pause = !pause;

        //if paused is true
        if (pause)
        {
            //set hand animation enabled to true (allow movement)
            handAnim.enabled = true;


        }
        else if (!pause)
        {
            //set hand animatino enabled to false (block movement)
            handAnim.enabled = false;
        }

    }

    private string Movement()
    {

        if (state)
        {
            //if state is true return stateOne reference 
            return stateOne;
        }
        else
        {
            //if state is false return stateTwo reference 
            return stateTwo;
        }

    }

    // Update is called once per frame
    void Update()
    {
        try
        {
            //If space if pressed swtich states
            if (Input.GetKeyDown(KeyCode.Space))
            {
                if (state)
                {
                    //false will return stateTwo reference 
                    state = false;
                }
                else
                {
                    //true will return stateOne reference
                    state = true;
                }
            }
            else
            {

                if (Input.GetKeyDown(KeyCode.D))
                {
                    Pause(true);
                    // Update controlDirection to increase by one frame (positive : forward)
                    handAnim.SetFloat(controlDirection, 1.0f);
                    // Play Movement play the state selected
                    handAnim.Play(Movement());
                }

                if (Input.GetKeyUp(KeyCode.D))
                {
                    // Rewind animation
                    Pause(false);
                }

                if (Input.GetKeyDown(KeyCode.A))
                {
                    Pause(true);
                    // Update controlDirection to decrease by one frame (negative : reverse)
                    handAnim.SetFloat(controlDirection, -1.0f);
                    handAnim.Play(Movement());
                }

                if (Input.GetKeyUp(KeyCode.A))
                {
                    // Rewind animation
                    Pause(false);
                }
            }
        }
        catch (Exception)
        {
            Debug.Log("Error");
        }
    }
}