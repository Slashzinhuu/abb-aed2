package ARN;

import exceptions.ChaveInvalidaException;
import exceptions.NoInvalidoException;
import exceptions.NoJaExisteException;
import exceptions.VerificacaoFalhouException;
import ABB.ABB;
import ARN.NoARN.Cor;

public class ARN extends ABB {

	private NoARN raiz;
	
	@Override
	public NoARN getRaiz() {
		return raiz;
	}
	
	public void setRaiz(NoARN raiz) {
		this.raiz = raiz;
	}
	
	public boolean isTerminal(NoARN no) {
		return (no.getNumero() == -1);
	}
	
	/**Encontra o menor no da subarvore**/
	public NoARN min (NoARN no) {
		if (!no.ehExterno()){ //se nao for no externo
			if (!no.getEsquerda().ehExterno()){ //se tem filho esquerdo
				return min(no.getEsquerda());//redireciona
			}else{ //se nao tem filho esquerdo
				return no; //eh o minimo
			}
		}else{ //se for no externo
			return null;
		}
	}
	
	/**Encontra o maior no da subarvore**/
	public NoARN max(NoARN no) {
		if (!no.ehExterno()){
			if (!no.getDireita().ehExterno()){
				return max(no.getDireita());
			}else{
				return no;
			}
		}else{
			return null;
		}
	}

	/**Busca um no**/
	public NoARN busca(int numero){
		return busca(numero, raiz);
	}
	public NoARN busca(int numero, NoARN no) {
		/* caso 1 = no passado e nulo
		 * caso 2 = no passado e o buscado
		 * caso 3 = no passado e maior que o buscado
		 * caso 4 = no passado e menor que o buscado*/
		NoARN aux = no;
		
		if (aux!= null){		
			if (numero == no.getNumero()) { //caso 2
				aux = no;
			} else if (numero < no.getNumero()) { //caso 3
				aux = busca(numero, no.getEsquerda());
			} else { //caso 4
				aux = busca(numero, no.getDireita());
			}
		}
		else{ //caso 1
			return null;
		}
		return aux;
	}
	
	/**Rotacao direita**/
	private void rotacaoDireita(NoARN y) {
		NoARN x = y.getEsquerda();
		y.setEsquerda(x.getDireita());
		x.getDireita().setPai(y);
		x.setPai(y.Pai());
		if (y.Pai() == null){
			setRaiz(x);
		} else {
			if (y.Pai().getDireita().equals(y)){
				y.Pai().setDireita(x);
			} else {
				y.Pai().setEsquerda(x);
			}
		}
		x.setDireita(y);
		y.setPai(x);
	}

	/**Rotacao esquerda**/
	private void rotacaoEsquerda(NoARN x) {
		NoARN y =  x.getDireita();
		x.setDireita(y.getEsquerda());
		y.getEsquerda().setPai(x);
		y.setPai(x.Pai());
		if (x.Pai() == null){
			setRaiz(y);
		} else {
			if (x.Pai().getEsquerda().equals(x)){
				x.Pai().setEsquerda(y);
			} else {
				x.Pai().setDireita(y);
			}
		}
		y.setEsquerda(x);
		x.setPai(y);
	}
	
	/**Insere em metodo ABB e rebalanceia a arvore**/
	public NoARN inserir(int numero) throws NoJaExisteException, ChaveInvalidaException {
		// w eh variavel auxiliar para a insercao em ABB
		NoARN w =  inserirEmABB(numero);
		
		// z eh o noh inserido na ARN
		NoARN z = w; 
		consertaInsere(z);
		return w;
	}
	private NoARN inserirEmABB(int numero) throws NoJaExisteException, ChaveInvalidaException {
		if (numero < 0){
			throw new ChaveInvalidaException();
		}
		if (this.raiz == null){ //se a raiz eh nula. Insere na raiz
			raiz = new NoARN(numero);
			raiz.setPai(null);
			return raiz;
		}
		else{ //se a raiz nao eh nula, procura o no na arvore
			try{
				return inserirEmABB (raiz, numero);
			}catch (NoJaExisteException e){
				throw new NoJaExisteException();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return raiz;
	}
	private NoARN inserirEmABB(NoARN no, int numero) throws NoJaExisteException, ChaveInvalidaException{
		//Se o valor a ser inserido for menor que o no atual
		if (numero < no.getNumero()){
			
			//Se ha subarvore esquerda, continua a busca
			if (!no.getEsquerda().ehExterno()){
				return inserirEmABB(no.getEsquerda(), numero);
			}
			//Se nao houver subarvore esquerda, insere
			else { 
				no.setEsquerda(new NoARN(numero));
				no.getEsquerda().setPai(no);
				return no.getEsquerda();
			}
		}
		//Se o valor a ser inserido for maior que o no atual
		else if(numero > no.getNumero()){
			
			//Se ha subarvore direita, continua a busca
			if (!no.getDireita().ehExterno()){
				return inserirEmABB(no.getDireita(), numero);
			}
			//Se nao houver subarvore direita, insere
			else {
				no.setDireita(new NoARN (numero));
				no.getDireita().setPai(no);
				return no.getDireita();
			}
		}
		//Se o valor a ser inserido for igual ao no atual
		else{
			throw new NoJaExisteException();
		}
	}
	private void consertaInsere(NoARN z) {
		//enquanto z nao for a raiz e o pai de z for vermelho
		while (!(z.equals(this.getRaiz())) && (z.Pai().eVermelho())) {
			//se o pai de z eh filho esquerdo do avo de z
			if (z.Pai().equals(z.Avo().getEsquerda())) {
				//checando o lado
				NoARN y = z.Tio();
				if(y.eVermelho()){
					//caso 1
					z.Pai().alteraCor(Cor.PRETO);
					y.alteraCor(Cor.PRETO);
					z.Avo().alteraCor(Cor.VERMELHO);
					z = z.Avo();
				} else {
					 /*casos 2 e 3 */ 
					if (z.Pai().getDireita().equals(z)){
						//caso 2
						z = z.Pai();
						rotacaoEsquerda(z);
					}
					//caso 3
					z.Pai().alteraCor(Cor.PRETO);
					z.Avo().alteraCor(Cor.VERMELHO);
					rotacaoDireita(z.Avo());
				}
			} else {
				//checando o lado
				NoARN y = z.Tio();
				if (y.eVermelho()){
					//caso 4
					z.Pai().alteraCor(Cor.PRETO);
					y.alteraCor(Cor.PRETO);
					z.Avo().alteraCor(Cor.VERMELHO);
					z = z.Avo();
				} else {
					/* casos 5 e 6 */
					if (z.Pai().getEsquerda().equals(z)){
						//caso 5
						z = z.Pai();
						rotacaoDireita(z);
					}
				//caso 6
				z.Pai().alteraCor(Cor.PRETO);
				z.Avo().alteraCor(Cor.VERMELHO);
				rotacaoEsquerda(z.Avo());
				}
			}
		}
		getRaiz().alteraCor(Cor.PRETO);
	}

	/**Remove em metodo ABB e rebalanceia a arvore**/
	public NoARN remover(int numero){
		if (raiz == null){
			System.out.println("Armaria omh, tais frescano cumigu�? \nTu nem botasse e ja quer tirar?!");
			return null;
		}
		NoARN w = new NoARN();
		/*
		 * O metodo precisa saber quem eh o no EFETIVAMENTE removido e saber se ele eh "nao externo & preto".
		 * Em seguida, o metodo chama o ConsertaRemove para o no QUE SUBSTITUIU O NO EFETIVAMENTE REMOVIDO.
		 */
		NoARN x = removeEmABB(numero, w);
		if (!w.ehExterno() && !(w.eVermelho())){
			System.out.println("CONSERRRRRRTA");
			consertaRemove(x);
		}
		return x;
	}
	private NoARN removeEmABB(int numero, NoARN w) {
		try {
			return removeEmABB(numero, raiz, w);
		} catch (NoInvalidoException e) {
			e.printStackTrace();
		}
		return null;
	}
	private NoARN removeEmABB(int numero, NoARN no, NoARN w) throws NoInvalidoException {
		NoARN aux = no;
		NoARN ok = null;
		if (aux != null) {
			//Se o no a ser removido for menor que o no atual
			if (numero < no.getNumero()){
				ok = removeEmABB(numero, no.getEsquerda(), w);
			}
			//Se o no a ser removido for maior que o no atual
			else if (numero > no.getNumero()){
				ok = removeEmABB(numero, no.getDireita(), w);
			}
			//Se o no a ser removido for o atual
			else if (numero == no.getNumero()){
				if(!no.getEsquerda().ehExterno() && !no.getDireita().ehExterno()) { //tem os dois filhos
					aux = min(no.getDireita());
					no.setNumero(aux.getNumero());
					ok = removeEmABB(aux.getNumero(), no.getDireita(), w);
				}else{ //no tem um ou nenhum filho
					aux = no; //guarda apontador do no modificado
					
					if (!aux.getEsquerda().ehExterno()){ //se so tem filho na esquerda
						NoARN pai = aux.Pai();
						if (pai.getEsquerda().equals(aux)){//se aux eh filho esquerdo do pai
							w = pai.getEsquerda(); //Salvo em W a referencia do no efetivamente excluido
							pai.setEsquerda(aux.getEsquerda()); //pai aponta pro neto
							pai.getEsquerda().setPai(pai); //antigo neto (agora filho) aponta pro pai
							ok = pai.getEsquerda();
						} else { //se eh filho direito do pai
							w = pai.getDireita(); //Salvo em W a referencia do no efetivamente excluido
							pai.setDireita(aux.getEsquerda()); //pai aponta pro neto
							pai.getDireita().setPai(pai);
							ok = pai.getDireita();
						}
					} else { //se so tem filho direito ou nenhum filho
						NoARN pai = aux.Pai();
						if (pai.getEsquerda().equals(aux)){ //se for filho esquerdo do pai
							w = pai.getEsquerda(); //Salvo em W a referencia do no efetivamente excluido
							pai.setEsquerda(aux.getDireita()); //ponteiro esquerdo do pai aponta pro neto
							pai.getEsquerda().setPai(pai); //ponteiro do neto aponta pro pai
							ok = pai.getEsquerda();
						} else {
							w = pai.getDireita(); //Salvo em W a referencia do no efetivamente excluido
							pai.setDireita(aux.getDireita()); //pai aponta pro neto ou para o no vazio a direita
							pai.getDireita().setPai(pai); //antigo neto (agora filho) aponta pro pai
							ok = pai.getDireita();
						}
					}
				}
			}else {
				ok = null;
			}
		}else{
			ok = null;
		}
		return ok;
	}
	private void consertaRemove(NoARN x) {
		while(!(x.equals(this.getRaiz())) && !(x.eVermelho())){
			if (x.equals(x.Pai().getEsquerda())){
				//checando o lado
				NoARN w = x.Pai().getDireita();
				if (w.eVermelho()){ //irmao vermelho
					//caso 1
					w.alteraCor(Cor.PRETO);
					x.Pai().alteraCor(Cor.VERMELHO);
					rotacaoEsquerda(x.Pai());
					w = x.Pai().getDireita();
				}
				if (!(w.getEsquerda().eVermelho() || w.getDireita().eVermelho())) {
					//caso 2
					w.alteraCor(Cor.VERMELHO);
					x = x.Pai();
				} else {
					if (!(w.getDireita().eVermelho())){
						//caso 3
						w.getEsquerda().alteraCor(Cor.PRETO);
						w.alteraCor(Cor.VERMELHO);
						rotacaoDireita(w);
						w = x.Pai().getDireita();
					}
					//caso 4
					w.alteraCor(x.Pai().getCor());
					x.Pai().alteraCor(Cor.PRETO);
					w.getDireita().alteraCor(Cor.PRETO);
					rotacaoEsquerda(x.Pai());
					x = raiz;
				}
			}else {
				//checando o lado
				NoARN w = x.Pai().getEsquerda();
				if (w.eVermelho()){ //irmao vermelho
					//caso 5
					w.alteraCor(Cor.PRETO);
					x.Pai().alteraCor(Cor.VERMELHO);
					rotacaoDireita(x.Pai());
					w = x.Pai().getEsquerda();
				}
				System.out.println(w.getEsquerda().getNumero());
				if (!(w.getDireita().eVermelho() || w.getEsquerda().eVermelho())) {
					//caso 6
					w.alteraCor(Cor.VERMELHO);
					x = x.Pai();
				} else {
					if (!(w.getEsquerda().eVermelho())){
						//caso 7
						w.getDireita().alteraCor(Cor.PRETO);
						w.alteraCor(Cor.VERMELHO);
						rotacaoEsquerda(w);
						w = x.Pai().getEsquerda();
					}
					//caso 8
					w.alteraCor(x.Pai().getCor());
					x.Pai().alteraCor(Cor.PRETO);
					w.getEsquerda().alteraCor(Cor.PRETO);
					rotacaoDireita(x.Pai());
					x = raiz;
				}
			}
		}
		x.alteraCor(Cor.PRETO);
	}

	/**Imprime os nos da Arvore percorrendo em ordem**/
	public void print(){
		visitarEmOrdem(getRaiz());
	}
	private void visitarEmOrdem(NoARN node) {
		if (node != null){
			this.visitarEmOrdem(node.getEsquerda());
			System.out.println(node.getNumero());
			System.out.println(node.getCor().name() + "\n");
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
			System.out.println(node.getCor().name() + "\n");
		}
	}
	
	/**Imprime os nos da Arvore percorrendo em pre-ordem**/
	public void printPre(){
		visitarPreOrdem(getRaiz());
	}
	private void visitarPreOrdem(NoARN node) {
		if (node != null){
			System.out.println(node.getNumero());
			System.out.println(node.getCor().name() + "\n");
			visitarPreOrdem(node.getEsquerda());
			visitarPreOrdem(node.getDireita());
		}
	}
	
	public boolean arvoreRubroNegraValida() throws VerificacaoFalhouException{
		if (raiz == null){
			System.out.println("Armaria omh, tais frescano cumigu�? Bote um no ai, va la...");
			return false;
		}
		if (raiz.getCor().equals(Cor.PRETO)){
			return (verificaNo(raiz) > 0);	
		} else {
			System.out.println("Violacao da Cor da Raiz");
			return false;
		}
	}
	
	public int verificaNo(NoARN no) throws VerificacaoFalhouException{
		/*
		1.	[x]Coloracao � Todo no possui uma e apenas uma de duas cores: vermelho ou preto.
		2.	[x]Raiz � A cor do no raiz e PRETA.
		3.	[x]No Externo � Todo no externo possui cor PRETA.
		4.	[x]No Interno � Todo no interno associado a cor VERMELHA possui dois filhos de cor PRETA.
		5.	[x]Profundidade � Para cada no, todos os caminhos do no a um no externo possuem o mesmo numero de nos associados a cor PRETA.
		6.  [x]Arvore Binaria de Busca - Todo filho esquerdo tem que ser menor que o pai e todo filho direito tem que ser maior.
		 */
		int altEsq, altDir;
		if (!(no.ehExterno())){
			NoARN filhoEsquerdo = no.getEsquerda();
			NoARN filhoDireito = no.getDireita();
			
			//Pai vermelho com filho vermelho
			if (no.eVermelho()){
				if (filhoEsquerdo.eVermelho() || filhoDireito.eVermelho()){
					System.out.println("Violacao de Vermelho");
					throw new VerificacaoFalhouException();
//					return 0;
				}
			}
			
			altEsq = verificaNo(filhoEsquerdo);
			altDir = verificaNo(filhoDireito);
			
			//Arvore Binaria de Busca
			if (!filhoEsquerdo.ehExterno() && filhoEsquerdo.getNumero() >= no.getNumero()
					|| !filhoDireito.ehExterno() && filhoDireito.getNumero() <= no.getNumero()){
				System.out.println("Violacao de ABB");
				throw new VerificacaoFalhouException();
//				return 0;
			}
			
			//Altura negra
			if (altEsq != 0 && altDir != 0 && altEsq != altDir){
				System.out.println("Violacao de Altura Negra");
				throw new VerificacaoFalhouException();
//				return 0;
			}
			
			if (altEsq != 0 && altDir != 0){
				if (no.eVermelho()){
					return altEsq;
				}else{
					return altEsq+1;
				}
			}
		}else { //se for no externo
			if (no.getCor().equals(Cor.PRETO)){
				return 1;
			} else{
				System.out.println("Violacao de Cor do No Externo");
				throw new VerificacaoFalhouException();
//				return 0;
			}
		}
		return -1;
	}
}
