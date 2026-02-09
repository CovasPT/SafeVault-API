// 1. Definimos os Tipos possiveis (Union Type - melhor que Enum aqui)
export type Category = 'backend' | 'frontend' | 'security' | 'devops';

// 2. A Interface Imutável
export interface Skill {
    readonly id: number;         // readonly = final do Java
    readonly name: string;
    readonly category: Category; // Só aceita aqueles valores acima
    readonly winRate: number;    // Tipo o Premier do CS2 (0-100)
}

// Definimos o que é a configuração visual
interface CategoryConfig {
    color: string;
    label: string;
}

// O "Mapa" de Estratégia (Substituto do Switch)
// Record<Category, ...> obriga-te a definir TODAS as categorias. 
// Se apagares o 'security' aqui, o código não compila (segurança pura!)
const categorySettings: Record<Category, CategoryConfig> = {
    backend: { color: '#ef4444', label: 'Backend' },   // Vermelho (T)
    frontend: { color: '#3b82f6', label: 'Frontend' }, // Azul (CT)
    security: { color: '#22c55e', label: 'Security' }, // Verde
    devops: { color: '#eab308', label: 'DevOps' }      // Amarelo
};

// --- COMO USAR ---
// Imagina que tens uma skill
const mySkill: Skill = { id: 1, name: 'Java', category: 'backend', winRate: 85 };

// Em vez de switch... acedes direto:
const skillColor = categorySettings[mySkill.category].color; 
// Resultado: '#ef4444'