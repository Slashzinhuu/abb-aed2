package ARN;

import exceptions.NoJaExisteException;
import ABB.ABB;
import ABB.No;
import ARN.NoARN.Cor;

public class ARN extends ABB {

	@Override
	public NoARN getRaiz() {
		return (NoARN) super.getRaiz();
	}
	
	public void insereEmABB(int chave) throws NoJaExisteException {
		super.inserir(chave);
	}

	public No inserir(int numero) throws NoJaExisteException {
		// w eh variavel auxiliar para a insercao em ABB
		NoARN w = (NoARN) super.inserir(numero);
		w.alteraCor(Cor.VERMELHO);
		// z eh o noh inserido na ARN
		NoARN z = w; 
		consertaInsere(z);
		return w;
	}

	private void consertaInsere(NoARN no) {
		
		while (!(no.equals((NoARN)super.getRaiz()) && (no.Pai().getCor()
				.equals(Cor.VERMELHO)))) {
			
			if (no.Pai().equals(no.Pai().Pai().getEsquerda())) {
				
				// caso 1
				if(no.Tio().getCor().equals(Cor.VERMELHO)){ 
					no.Pai().alteraCor(Cor.PRETO);
					no.Tio().alteraCor(Cor.PRETO);
					no.Avo().alteraCor(Cor.VERMELHO);
					no = no.Avo();
				} else /*caso 2 */ { 
					if (no.Pai().getDireita().equals(no)){
						no = no.Pai();
						rotacaoEsquerda(no); 
					}
					
					//caso 3
					
					no.Pai().alteraCor(Cor.PRETO);
					no.Avo().alteraCor(Cor.VERMELHO);
					rotacaoDireita(no.Avo());
				}
				
				
			} else {
				
				if (no.Tio().getCor().equals(Cor.VERMELHO)){ 
					//caso 4
					no.Pai().alteraCor(Cor.PRETO);
					no.Tio().alteraCor(Cor.PRETO);
					no.Avo().alteraCor(Cor.VERMELHO);
					no = no.Avo();
				} else /* caso 5 */ {
					if (no.Pai().getEsquerda().equals(no)){ 
						no = no.Pai();
						rotacaoDireita(no);
					}
				}
				
				no.Pai().alteraCor(Cor.PRETO);
				no.Avo().alteraCor(Cor.VERMELHO);
				rotacaoEsquerda(no.Avo());
				
			}

		}
		
		getRaiz().alteraCor(Cor.PRETO);

	}

	private void rotacaoDireita(NoARN no) {
		NoARN aux =  no.Pai();
		no.setPai(no.getEsquerda());
		no.getEsquerda().setPai(aux);
		NoARN esquerdaAux =  no.getEsquerda();
		no.setEsquerda(no.getEsquerda().getEsquerda());
		esquerdaAux.setEsquerda(esquerdaAux.getDireita() );
		esquerdaAux.setDireita(no.getDireita());
		no.setDireita((esquerdaAux));
	}

	private void rotacaoEsquerda(NoARN no) {
		NoARN aux =  no.Pai();
		no.setPai(no.getEsquerda());
		no.getDireita().setPai(aux);
		NoARN direitaAux =  no.getDireita();
		no.setDireita(no.getDireita().getDireita());
		direitaAux.setDireita(direitaAux.getDireita() );
		direitaAux.setEsquerda(no.getEsquerda());
		no.setEsquerda((direitaAux));
	}
	
	/*Imprime os nos da Arvore percorrendo em ordem*/
	public void print(){
		visitarEmOrdem(getRaiz());
	}
	
	private void visitarEmOrdem(NoARN node) {
		if (node != null){
			this.visitarEmOrdem(node.getEsquerda());
			System.out.println(node.getNumero());
			System.out.println(node.getCor().name());
			visitarEmOrdem(node.getDireita());
		}
	}
	
	/**Imprime os nos da Arvore percorrendo em pos-ordem**/
	public void printPos(){
		visitarPosOrdem(getRaiz());
	}
	
	private void visitarPosOrdem(NoARN node) {
		if (node != null){
			visitarPosOrdem(node.getEsquerda());
			visitarPosOrdem(node.getDireita());
			System.out.println(node.getNumero());
			System.out.println(node.getCor().name());
		}
	}
	
	/**Imprime os nos da Arvore percorrendo em pre-ordem**/
	public void printPre(){
		visitarPreOrdem(getRaiz());
	}
	
	private void visitarPreOrdem(NoARN node) {
		if (node != null){
			System.out.println(node.getNumero());
			System.out.println(node.getCor().name());
			visitarPreOrdem(node.getEsquerda());
			visitarPreOrdem(node.getDireita());
		}
	}
}