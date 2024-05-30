using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class PauseMenu : MonoBehaviour
{
    public GameObject pauseMenuUI;

    void Start()
    {
        pauseMenuUI.SetActive(false);
    }

    private void Update()
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            TogglePauseMenu();
        }
    }

    public void TogglePauseMenu()
    {
        pauseMenuUI.SetActive(!pauseMenuUI.activeSelf);

        Time.timeScale = pauseMenuUI.activeSelf ? 0f : 1f;
    }

    public void ContinueGame()
    {
        TogglePauseMenu();
    }

    public void QuitGame()
    {
        SceneManager.LoadScene("_START_MENU_");
    }
}
