package br.com.fiap.techchallenge.producao.core.usecases.pedido;

import br.com.fiap.techchallenge.producao.core.domain.entities.ItemPedido;
import br.com.fiap.techchallenge.producao.core.domain.entities.Pedido;
import br.com.fiap.techchallenge.producao.core.domain.exceptions.EntityNotFoundException;
import br.com.fiap.techchallenge.producao.core.dtos.*;
import br.com.fiap.techchallenge.producao.core.domain.entities.enums.StatusPedidoEnum;
import br.com.fiap.techchallenge.producao.core.ports.in.pedido.CriaPedidoInputPort;
import br.com.fiap.techchallenge.producao.core.ports.out.pedido.BuscarPedidoOutputPort;
import br.com.fiap.techchallenge.producao.core.ports.out.pedido.CriaPedidoOutputPort;

import java.util.List;

public class CriaPedidoUseCase implements CriaPedidoInputPort {

    private final CriaPedidoOutputPort criaPedidoOutputPort;

    private final BuscarPedidoOutputPort buscarPedidoOutputPort;

    public CriaPedidoUseCase(CriaPedidoOutputPort criaPedidoOutputPort, BuscarPedidoOutputPort buscarPedidoOutputPort) {
        this.criaPedidoOutputPort = criaPedidoOutputPort;
        this.buscarPedidoOutputPort = buscarPedidoOutputPort;

    }

    @Override
    public PedidoDTO criar(CriaPedidoDTO pedidoIn) {
        verificaPedidoExiste(pedidoIn);
        var pedido = new Pedido(pedidoIn.codigo(), StatusPedidoEnum.RECEBIDO);
        adicionaItensPedido(pedido, pedidoIn.itens());
        return criaPedidoOutputPort.criar(new PedidoDTO(pedido));
    }

    private void verificaPedidoExiste(CriaPedidoDTO pedidoIn) {
        try {
            buscarPedidoOutputPort.buscarPorCodigo(pedidoIn.codigo());
        } catch(EntityNotFoundException ignored) {

        }
    }

    private void adicionaItensPedido(Pedido pedido, List<ItemPedidoDTO> itensPedidoIn) {
        itensPedidoIn.forEach(item ->
            pedido.addItemPedido(
                new ItemPedido(item.nome(), item.descricao(), item.quantidade())
            )
        );
    }
}
