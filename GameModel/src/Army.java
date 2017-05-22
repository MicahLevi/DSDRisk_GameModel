
public class Army {
	public int owner_id;
	public int num_armies;
	public int country_id;
	public Army() {
		owner_id = 0;
		country_id = 0;
		num_armies = 0;
	}
	public Army(int player_id, int armies,int countryId) {
		owner_id   = player_id;
		num_armies = armies;
		country_id = countryId;
		
	}
}