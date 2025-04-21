package io.proleap.cobol.analysis.codeabstract;

import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext; 
import io.proleap.cobol.CobolParser.ParagraphContext;
import io.proleap.cobol.CobolParser.ProcedureDivisionContext;
import io.proleap.cobol.CobolParser.ProcedureNameContext; 
import io.proleap.cobol.analysis.util.NamingUtils;
import io.proleap.cobol.asg.metamodel.ASGElement; 
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.metamodel.call.Call;
import io.proleap.cobol.asg.metamodel.call.Call.CallType;
import io.proleap.cobol.asg.metamodel.call.CommunicationDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.DataDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.FileControlEntryCall;
import io.proleap.cobol.asg.metamodel.call.ProcedureCall;
import io.proleap.cobol.asg.metamodel.call.ReportDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.ScreenDescriptionEntryCall;
import io.proleap.cobol.asg.metamodel.call.TableCall;
import io.proleap.cobol.asg.metamodel.procedure.Paragraph;
import io.proleap.cobol.asg.metamodel.registry.ASGElementRegistry;

public class ConcreteCodeVisitor  extends AbstractCodeVisitor<Object> {

	@Override
	public Boolean createResult() {
		return null;
	}

    protected ParserRuleContext getCalledParseTree(final Call call) {
		final CallType callType = call.getCallType();
		final ParserRuleContext result;

		switch (callType) {
		case COMMUNICATION_DESCRIPTION_ENTRY_CALL:
			result = ((CommunicationDescriptionEntryCall) call).getCommunicationDescriptionEntry().getCtx();
			break;
		case DATA_DESCRIPTION_ENTRY_CALL:
			result = ((DataDescriptionEntryCall) call).getDataDescriptionEntry().getCtx();
			break;
		case FILE_CONTROL_ENTRY_CALL:
			result = ((FileControlEntryCall) call).getFileControlEntry().getCtx();
			break;
		case PROCEDURE_CALL:
			result = ((ProcedureCall) call).getParagraph().getCtx();
			break;
		case REPORT_DESCRIPTION_ENTRY_CALL:
			result = ((ReportDescriptionEntryCall) call).getReportDescriptionEntry().getCtx();
			break;
		case SCREEN_DESCRIPTION_ENTRY_CALL:
			result = ((ScreenDescriptionEntryCall) call).getScreenDescriptionEntry().getCtx();
			break;
		case TABLE_CALL:
			result = ((TableCall) call).getDataDescriptionEntry().getCtx();
			break;
		case UNDEFINED_CALL:
			result = null;
			break;
		default:
			result = null;
		}

		return result;
	}
	private static final String COMMA = ",";

    @Override
    public Boolean visitProcedureDivision(final ProcedureDivisionContext ctx) {
		String id = idRegistry.assureId(ctx);
      
        return visitChildren(ctx);
    }

    @Override
	public Boolean visitProcedureName(final ProcedureNameContext ctx) {
      
		final ASGElement asgElement = getASGElement(ctx);
		String id = null;
		if (asgElement != null && asgElement instanceof Call) {
			final Call call = (Call) asgElement;
			final Call unwrappedCall = call.unwrap();
			id = getCallId(unwrappedCall);

			 if (id != null && !id.isEmpty()) {
				idRegistry.assureId(ctx);
			}
		} 
        //System.out.printf("(ProcedureName) ID=%s name=%s\n", id,ctx.getText()); 
		return visitChildren(ctx);
	}

    @Override
	public Boolean visitParagraph(final ParagraphContext ctx) {
        String id = idRegistry.assureId(ctx);
       final Paragraph paragraph = (Paragraph) getASGElement(ctx);
		final String callerNames = getCallerNames(paragraph.getCalls());
        System.out.printf("(visitParagraph) ID = %s name = %s | callernames = %s\n", id,paragraph.getName(), callerNames);

		return visitChildren(ctx);
    }
	 
    protected ASGElement getASGElement(final ParserRuleContext ctx) {
		final Program program = compilationUnit.getProgram();
		final ASGElementRegistry asgElementRegistry = program.getASGElementRegistry();
		return asgElementRegistry.getASGElement(ctx);
	}

    @SuppressWarnings("unchecked")
	protected String getCallerIds(final List<?> calls) {
		return ((List<Call>) calls).stream().map(call -> idRegistry.assureRelativeId(call.getCtx(), compilationUnit))
				.collect(Collectors.joining(COMMA));
	}

    @SuppressWarnings("unchecked")
	protected String getCallerNames(final List<?> calls) {
		return ((List<Call>) calls).stream()
				.map(call -> NamingUtils.determineFullQualifiedName(call.getCtx(), compilationUnit))
				.collect(Collectors.joining(COMMA));
	}

    protected String getCallId(final Call call) {
		final ParserRuleContext calledParseTree = getCalledParseTree(call);
		final String result;

		if (calledParseTree == null) {
			result = null;
		} else {
			result = idRegistry.assureRelativeId(calledParseTree, compilationUnit);
		}

		return result;
	}

}
