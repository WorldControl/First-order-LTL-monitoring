package formulaa.foltl;

import formulaa.ForallQuantifiedFormula;
import formulaa.foltl.semantics.FoLtlAssignment;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Class that represents a FOLTL across-state forall quantified formula.
 * <br>
 * Created by Simone Calciolari on 06/08/15.
 * @author Simone Calciolari
 */
public class FoLtlAcrossForallFormula extends FoLtlQuantifiedFormula implements FoLtlAcrossQuantifiedFormula,
		ForallQuantifiedFormula {

	public FoLtlAcrossForallFormula(FoLtlFormula nestedFormula, FoLtlVariable quantifiedVariable){
		super(nestedFormula, quantifiedVariable);
	}

	@Override
	public formulaa.FormulaType getFormulaType(){
		return formulaa.FormulaType.ACROSS_FORALL;
	}

	@Override
	public String stringOperator(){
		return "xsForall";
	}

	@Override
	public FoLtlFormula temporalExpansion(HashSet<FoLtlConstant> domain, FoLtlAssignment assignment){
		FoLtlFormula res = null;
		FoLtlFormula nested = this.getNestedFormula().clone();
		FoLtlVariable v = this.getQuantifiedVariable();

		Iterator<FoLtlConstant> i;

		if (!v.getSort().isEmpty()){
			i = v.getSort().iterator();
		} else {
			i = domain.iterator();
		}

		while (i.hasNext()){
			FoLtlConstant c = i.next();
			assignment.put(v, c);

			FoLtlFormula temp;

			if (nested instanceof FoLtlTemporalFormula){
				temp = ((FoLtlTemporalFormula) nested).temporalExpansion(domain, assignment);
			} else {
				temp = nested.substitute(assignment);
			}

			if (res == null){
				res = temp;
			} else {
				res = new FoLtlTempAndFormula(temp, res);
			}

			assignment.remove(v);

		}

		return res;
	}

}
