package coup;

import data.Case;

public class CoupRock extends Coup {
	
	public Case caseTDep, caseTArr;
	
	public CoupRock(Case caseRDep, Case caseRArr, Case caseTDep, Case caseTArr) {
		super(caseRDep, caseRArr);
		this.caseTDep = caseTDep;
		this.caseTArr = caseTArr;
	}
	
	@Override
	public Case[] getCases() {
		return new Case[] {getCaseDepart(), getCaseArrivee(), caseTDep, caseTArr};
	}
	
	public void jouerCoup() {
		super.jouerCoup();
		caseTArr.setPiece(caseTDep.getPiece());
		caseTArr.getPiece().setCase(caseTArr);
		caseTDep.setPiece(null);
	}
	
	public void getBack() {
		super.getBack();
		caseTDep.setPiece(caseTArr.getPiece());
		caseTDep.getPiece().setCase(caseTDep);
		caseTArr.setPiece(null);
	}
	
}
