package coup;

import data.Case;

public class CoupEp extends Coup {
	
	public Case ep;
	
	/**Constructeur */
	public CoupEp(Case caseDep, Case caseArr, Case ep) {
		super(caseDep, caseArr);
		this.prise = ep.getPiece();
		this.ep = ep;
	}
	
	@Override
	public Case[] getCases() {
		return new Case[] {getCaseDepart(), getCaseArrivee(), ep};
	}
	
	public void jouerCoup() {
		super.jouerCoup();
		ep.setPiece(null);
		prise.setCase(null);
	}
	
	public void getBack() {
		caseDep.setPiece(caseArr.getPiece());
		caseArr.setPiece(null);
		caseDep.getPiece().setCase(caseDep);
		ep.setPiece(getPrise());
		getPrise().setCase(ep);
	}
}
