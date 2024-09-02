using UnityEngine;

public class CameraMover : MonoBehaviour
{
    public float speed = 5.0f;
    public float amplitude = 5.0f;
    public float frequency = 1.0f;

    private Vector3 startPosition;

    void Start()
    {
        startPosition = transform.position;
    }

    void Update()
    {
        float x = startPosition.x + Mathf.Sin(Time.time * frequency) * amplitude;
        float y = startPosition.y;
        float z = startPosition.z + speed * Time.time;

        transform.position = new Vector3(x, y, z);
    }
}
