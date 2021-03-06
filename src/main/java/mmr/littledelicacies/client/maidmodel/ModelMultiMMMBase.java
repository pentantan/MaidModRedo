package mmr.littledelicacies.client.maidmodel;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mmr.littledelicacies.api.IMaidAnimation;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * MMMの実験コードを含む部分。
 * ModelMultiBaseに追加するに足りるかをここで実験。
 * このクラスにある機能は予告なく削除される恐れが有るためご留意下さい。
 */
public abstract class ModelMultiMMMBase<T extends LivingEntity> extends ModelMultiBase<T> {

	public Map<String, EquippedStabilizer> stabiliser;

	/**
	 * 削除予定変数使わないで下さい。
	 */
	@Deprecated
	public float onGround;
	/**
	 * 削除予定変数使わないで下さい。
	 */
	@Deprecated
	public float heldItemLeft;
	/**
	 * 削除予定変数使わないで下さい。
	 */
	@Deprecated
	public float heldItemRight;


	public ModelMultiMMMBase() {
		super();
	}
	public ModelMultiMMMBase(float pSizeAdjust) {
		super(pSizeAdjust);
	}
	public ModelMultiMMMBase(float pSizeAdjust, float pYOffset, int pTextureWidth, int pTextureHeight) {
		super(pSizeAdjust, pYOffset, pTextureWidth, pTextureHeight);
	}

	/**
	 * mainFrameに全てぶら下がっているならば標準で描画する。
	 */
	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		ImmutableList.of(this.mainFrame).forEach((p_228292_8_) -> {
			p_228292_8_.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});
		//renderStabilizer(pEntityCaps, par2, par3, ticksExisted, pheadYaw, pheadPitch, par7);
	}

    /*public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.mainFrame);
    }*/


	/**
	 * 通常のレンダリング前に呼ばれる。
	 * @return falseを返すと通常のレンダリングをスキップする。
	 */
	public boolean preRender(float par2, float par3,
			float par4, float par5, float par6, float par7) {
		return true;
	}

	/**
	 * 通常のレンダリング後に呼ぶ。 基本的に装飾品などの自律運動しないパーツの描画用。
	 */
	public void renderExtention(float par2, float par3,
			float par4, float par5, float par6, float par7) {
	}

	/**
	 * スタビライザーの描画。 自動では呼ばれないのでrender内で呼ぶ必要があります。
	 */
/*	protected void renderStabilizer(IModelCaps pEntityCaps, float par2, float par3,
									float ticksExisted, float pheadYaw, float pheadPitch, float par7) {
		// スタビライザーの描画、doRenderの方がいいか？
		if (stabiliser == null || stabiliser.isEmpty() || render == null)
			return;

		GL11.glPushMatrix();
		for (Entry<String, EquippedStabilizer> le : stabiliser.entrySet()) {
			EquippedStabilizer les = le.getValue();
			if (les != null && les.equipPoint != null) {
				ModelStabilizerBase lsb = les.stabilizer;
				if (lsb.isLoadAnotherTexture()) {
					Minecraft.getInstance().getTextureManager().bindTexture(lsb.getTexture());
				}
				//les.equipPoint.loadMatrix();
				lsb.render(this, null, par2, par3, ticksExisted, pheadYaw, pheadPitch, par7);
			}
		}
		GL11.glPopMatrix();
	}*/

	/**
	 * 初期ロード時に実行
	 */
    public abstract void setAnimations(float par1, float par2, float ageInTicks, float pHeadYaw, float pHeadPitch, T pEntityCaps, IMaidAnimation animation);

	public void setDefaultPause() {
	}

	public void setDefaultPause(T entity, float par1, float par2, float pTicksExisted,
								float pHeadYaw, float pHeadPitch) {
		setDefaultPause();
	}

	// Actors実験区画
	// このへん未だ未整理
	public void renderFace(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	}

	public void renderBody(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	}
	/**
	 * 表情をテクスチャのUVマップを変えることで表現
	 * @param pIndex
	 */
	public int setFaceTexture(int pIndex) {
		// u = (int)(pIndex % 2) * 32 / 64
		// v = (int)(pIndex / 2) * 32 / 32
		GL11.glTranslatef(((pIndex & 0x01) * 32) / textureWidth, (((pIndex >>> 1) & 0x01) * 16) / textureHeight , 0F);
		return pIndex / 4;
	}

}
