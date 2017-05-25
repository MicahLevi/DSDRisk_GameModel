
public class TerritoryInfo {
	public int owner_id;
	public int num_armies;
	public int country_id;
	public TerritoryInfo() {
		owner_id = -1;
		country_id = 0;
		num_armies = 0;
	}
	public TerritoryInfo(int player_id, int armies,int countryId) {
		owner_id   = player_id;
		num_armies = armies;
		country_id = countryId;
		
	}
}