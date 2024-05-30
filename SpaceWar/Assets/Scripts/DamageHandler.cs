using UnityEngine;
using System.Collections;

public class DamageHandler : MonoBehaviour {

	public int health = 1;
	public bool isEnemy = true;
	public bool isBullet = false;
	public static int enemiesDestroyed = 0;
	public static int maxScoore = 0;

	public float invulnPeriod = 0;
	float invulnTimer = 0;
	int correctLayer;

	SpriteRenderer spriteRend;

	public GameObject explosion;

	void Start() {
		correctLayer = gameObject.layer;

		// This only get the renderer on the parent object
		// In other words, it doesn't work for children. I.E. "enemy01"
		spriteRend = GetComponent<SpriteRenderer>();

		if(spriteRend == null) {
			spriteRend = transform.GetComponentInChildren<SpriteRenderer>();

			if(spriteRend==null) {
				Debug.LogError("Object '"+gameObject.name+"' has no sprite renderer.");
			}
		}
	}

	void OnTriggerEnter2D() {
		health--;

		if(invulnPeriod > 0) {
			invulnTimer = invulnPeriod;
			gameObject.layer = 10;
		}
	}

	void Update() {

		if(invulnTimer > 0) {
			invulnTimer -= Time.deltaTime;

			if(invulnTimer <= 0) {
				gameObject.layer = correctLayer;
				if(spriteRend != null) {
					spriteRend.enabled = true;
				}
			}
			else {
				if(spriteRend != null) {
					spriteRend.enabled = !spriteRend.enabled;
				}
			}
		}

		if(health <= 0) {
			Die();
		}
	}

	void Die() {
		if(!isBullet) 
		{
			Instantiate(explosion, transform.position, Quaternion.identity);
		}
		Destroy(gameObject);
		if (isEnemy) 
		{
			enemiesDestroyed++;
			if (enemiesDestroyed > maxScoore) {
				maxScoore = enemiesDestroyed;
			}
		}
	}

	void OnGUI() {
		GUI.color = Color.green;
        GUI.Label(new Rect(0, 20, 200, 20), "Enemies Destroyed: " + enemiesDestroyed);
        GUI.Label(new Rect(0, 40, 200, 20), "Max Scoore: " + maxScoore);
    }
}
